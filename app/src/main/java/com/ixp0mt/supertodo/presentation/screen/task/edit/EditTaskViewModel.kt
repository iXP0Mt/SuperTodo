package com.ixp0mt.supertodo.presentation.screen.task.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.R
import com.ixp0mt.supertodo.domain.util.SettingConstant
import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.usecase.element.GetElementByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTaskByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.task.SaveEditTaskUseCase
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.presentation.navigation.Routes
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.ElementEditViewModel
import com.ixp0mt.supertodo.presentation.util.TypeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val saveEditTaskUseCase: SaveEditTaskUseCase,
    private val getElementByLocationUseCase: GetElementByLocationUseCase
) : ElementEditViewModel() {
    private val _taskInfo = MutableLiveData<TaskInfo>(TaskInfo.empty())
    val taskInfo: LiveData<TaskInfo> = _taskInfo

    private val _nameTask = MutableLiveData<String>("")
    val nameTask: LiveData<String> = _nameTask

    private val _description = MutableLiveData<String>("")
    val description: LiveData<String> = _description

    private val _nameLocation = MutableLiveData<String>("")
    val nameLocation: LiveData<String> = _nameLocation

    private val _idIcon = MutableLiveData<Int>(null)
    val idIcon: LiveData<Int> = _idIcon


    override fun getScreen(screenState: ScreenState): Screen? =
        screenState.currentScreen as? Screen.TaskEdit

    override fun getIdElementFromArgs(screenState: ScreenState): Long? =
        screenState.currentArgs[Routes.TaskEdit.ID]?.toLongOrNull()

    override suspend fun initElement(idElement: Long) {
        initTask(idElement)
    }

    public override fun clearScreenState() {
        super.clearScreenState()
        //_nameTask.value = ""
        //_description.value = ""
    }

    fun changeNameTask(nameTask: String) {
        if (nameTask.length <= SettingConstant.MAX_TASK_NAME_LENGTH)
            _nameTask.value = nameTask
    }

    fun changeDescription(description: String) {
        if (description.length <= SettingConstant.MAX_TASK_DESCRIPTION_LENGTH)
            _description.value = description
    }

    override fun checkAction(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.TaskEdit).buttons.onEach { button ->
            when (button) {
                TypeAction.ACTION_NAV_BACK -> handleBack()
                TypeAction.ACTION_SAVE -> if (isValidate()) saveEdit()
                else -> Unit
            }
        }.launchIn(scope)
    }

    private suspend fun initTask(idTask: Long) {
        if (getTaskInfo(idTask)) {
            _nameTask.value = _taskInfo.value!!.name
            _description.value = _taskInfo.value!!.description ?: ""

            if(_locationInfo.value == null) {
                _nameLocation.value = getNameLocation(_taskInfo.value!!.typeLocation, _taskInfo.value!!.idLocation ?: 0L)
                _idIcon.value = getIconIdLocation(_taskInfo.value!!.typeLocation)
            } else {
                _nameLocation.value = getNameLocation(_locationInfo.value!!.typeLocation, _locationInfo.value!!.idLocation)
                _idIcon.value = getIconIdLocation(_locationInfo.value!!.typeLocation)
            }
        }
    }

    private suspend fun getTaskInfo(idTask: Long): Boolean {
        val param = GetTaskByIdParam(idTask)
        val result = getTaskByIdUseCase(param)
        when {
            result.isSuccess -> {
                _taskInfo.value = result.getOrThrow()
                return true
            }

            result.isFailure -> {
                val e = result.exceptionOrNull()
                Log.d("ttt", "e: $e !!!")
                return false
            }
        }
        return false
    }

    private fun isValidate(): Boolean {
        if(_saveEditState.value == true) {
            handleBack()
            return false
        }
        if (_nameTask.value!!.isBlank()) {
            setErrorMsg("Пустое имя задачи")
            return false
        }
        return true
    }

    private fun saveEdit() {
        val editedTask = TaskInfo(
            idTask = _taskInfo.value!!.idTask,
            name = _nameTask.value!!,
            description = _description.value,
            typeLocation = if(_locationInfo.value == null) _taskInfo.value!!.typeLocation else _locationInfo.value!!.typeLocation,
            idLocation = if(_locationInfo.value == null) _taskInfo.value!!.idLocation else _locationInfo.value!!.idLocation,
            dateCreate = _taskInfo.value!!.dateCreate,
            dateEdit = System.currentTimeMillis(),
            dateArchive = _taskInfo.value!!.dateArchive,
            dateStart = _taskInfo.value!!.dateStart,
            dateEnd = _taskInfo.value!!.dateEnd,
            dateCompleted = _taskInfo.value!!.dateCompleted
        )

        viewModelScope.launch {
            val result = saveEditTaskUseCase.execute(editedTask)
            when {
                result.isSuccess -> {
                    val cho = result.getOrThrow()
                    Log.d("ttt", "edited rows = $cho")
                    _saveEditState.value = true
                    handleBack()
                }

                result.isFailure -> {
                    val e = result.exceptionOrNull()
                    Log.d("ttt", "Exception: $e")
                    setErrorMsg(e?.message)
                }
            }
        }
    }

    private suspend fun getNameLocation(typeLocation: TypeLocation, idLocation: Long): String {
        val param = LocationParam(typeLocation, idLocation)
        val result = getElementByLocationUseCase.execute(param)
        when {
            result.isSuccess -> {
                val element = result.getOrThrow()
                return element.name
            }
            result.isFailure -> {
                val e = result.exceptionOrNull()
                Log.d("ttt", "Exception: $e")
            }
        }
        return "NULL"
    }

    private fun getIconIdLocation(typeLocation: TypeLocation): Int {
        return when(typeLocation) {
            TypeLocation.FOLDER -> R.drawable.ic_folder
            TypeLocation.PROJECT -> R.drawable.ic_project
            TypeLocation.TASK -> R.drawable.ic_task
            TypeLocation.MAIN -> R.drawable.ic_home
            else -> R.drawable.baseline_delete_forever_24
        }
    }
}

