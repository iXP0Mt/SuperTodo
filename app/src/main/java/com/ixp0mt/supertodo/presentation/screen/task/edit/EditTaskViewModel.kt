package com.ixp0mt.supertodo.presentation.screen.task.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.util.SettingConstant
import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.usecase.task.GetTaskByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.task.SaveEditTaskUseCase
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
    private val saveEditTaskUseCase: SaveEditTaskUseCase
) : ElementEditViewModel() {
    private val _taskInfo = MutableLiveData<TaskInfo>(TaskInfo.empty())

    private val _nameTask = MutableLiveData<String>("")
    val nameTask: LiveData<String> = _nameTask

    private val _description = MutableLiveData<String>("")
    val description: LiveData<String> = _description


    override fun getScreen(screenState: ScreenState): Screen? =
        screenState.currentScreen as? Screen.TaskEdit

    override fun getIdElementFromArgs(screenState: ScreenState): Long? =
        screenState.currentArgs[Routes.TaskEdit.ID]?.toLongOrNull()

    override suspend fun initElement(idElement: Long) {
        initTask(idElement)
    }

    public override fun clearScreenState() {
        super.clearScreenState()
        _nameTask.value = ""
        _description.value = ""
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
            _description.value = _taskInfo.value?.description ?: ""
        }
    }

    private suspend fun getTaskInfo(idTask: Long): Boolean {
        val param = GetTaskByIdParam(idTask)
        val result = getTaskByIdUseCase.execute(param)
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
            typeLocation = _taskInfo.value!!.typeLocation,
            idLocation = _taskInfo.value!!.idLocation,
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
}



/*
@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val saveEditTaskUseCase: SaveEditTaskUseCase
) : ViewModel() {

    private val _backClick = MutableLiveData<Boolean?>(null)
    val backClick: LiveData<Boolean?> = _backClick

    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _saveEditState = MutableLiveData<Boolean>(false)
    //val saveEditState: LiveData<Boolean> = _saveEditState

    private val _taskInfo = MutableLiveData<TaskInfo>(TaskInfo.empty())

    private val _nameTask = MutableLiveData<String>("")
    val nameTask: LiveData<String> = _nameTask

    private val _description = MutableLiveData<String>("")
    val description: LiveData<String> = _description

    private lateinit var job: Job


    fun clearScreenState() {
        _saveEditState.value = false
        _backClick.value = null
        _errorMsg.value = null
        _nameTask.value = ""
        _description.value = ""
        closeStates()
    }

    fun changeNameTask(nameTask: String) {
        if (nameTask.length <= SettingConstant.MAX_TASK_NAME_LENGTH)
            _nameTask.value = nameTask
    }

    fun changeDescription(description: String) {
        if (description.length <= SettingConstant.MAX_TASK_DESCRIPTION_LENGTH)
            _description.value = description
    }

    fun initScreen(screenState: ScreenState) {
        val screen = screenState.currentScreen as? Screen.TaskEdit
        screen?.let {
            val idTask = screenState.currentArgs[Routes.TaskEdit.ID]?.toLongOrNull()
            idTask?.let {
                job = viewModelScope.launch {
                    initTask(it)
                    //screen.setTitle("Папка: ${_folderInfo.value?.name}")
                    checkAction(screen, this)
                }
            }
        }
    }

    private fun checkAction(screen: Screen.TaskEdit, scope: CoroutineScope) {
        screen.buttons.onEach { button ->
            when (button) {
                TypeAction.ACTION_NAV_BACK -> handleBack()
                TypeAction.ACTION_SAVE -> {
                    if (isValidate()) {
                        saveEdit()
                    }
                }
                else -> Unit
            }
        }.launchIn(scope)
    }

    private suspend fun initTask(idTask: Long) {

        if (getTaskInfo(idTask)) {
            _nameTask.value = _taskInfo.value!!.name
            _description.value = _taskInfo.value?.description ?: ""
        }

    }

    private suspend fun getTaskInfo(idTask: Long): Boolean {
        val param = GetTaskByIdParam(idTask)
        val result = getTaskByIdUseCase.execute(param)
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
            _errorMsg.value = "Пустое имя задачи"
            return false
        }
        return true
    }

    private fun saveEdit() {
        val editedTask = TaskInfo(
            idTask = _taskInfo.value!!.idTask,
            name = _nameTask.value!!,
            description = _description.value,
            typeLocation = _taskInfo.value!!.typeLocation,
            idLocation = _taskInfo.value!!.idLocation,
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
                    _errorMsg.value = e?.message
                }
            }
        }
    }

    fun clearErrorMsg() { _errorMsg.value = null }
    fun handleBack() { _backClick.value = _backClick.value?.not() ?: true }
    private fun closeStates() { job.cancel() }
}*/
