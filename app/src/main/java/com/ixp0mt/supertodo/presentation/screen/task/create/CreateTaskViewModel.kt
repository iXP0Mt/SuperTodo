package com.ixp0mt.supertodo.presentation.screen.task.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.util.SettingConstant
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.usecase.task.SaveNewTaskUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.ElementCreateViewModel
import com.ixp0mt.supertodo.presentation.util.TypeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val saveNewTaskUseCase: SaveNewTaskUseCase
) : ElementCreateViewModel() {

    private val _idNewTask = MutableLiveData<Long?>(null)
    val idNewTask: LiveData<Long?> = _idNewTask

    private val _nameTask = MutableLiveData<String>("")
    val nameTask: LiveData<String> = _nameTask

    private val _description = MutableLiveData<String>("")
    val description: LiveData<String> = _description


    public override fun clearScreenState() {
        super.clearScreenState()
        _idNewTask.value = null
        _nameTask.value = ""
        _description.value = ""
    }

    override fun getScreen(screenState: ScreenState): Screen? =
        screenState.currentScreen as? Screen.TaskCreate

    override fun checkAction(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.TaskCreate).buttons.onEach { button ->
            when (button) {
                TypeAction.ACTION_NAV_BACK -> handleBack()
                TypeAction.ACTION_SAVE -> if(isValidate()) save()
                else -> Unit
            }
        }.launchIn(scope)
    }

    fun changeNameTask(nameTask: String) {
        if(nameTask.length <= SettingConstant.MAX_TASK_NAME_LENGTH)
            _nameTask.value = nameTask
    }

    fun changeDescription(description: String) {
        if(description.length <= SettingConstant.MAX_TASK_DESCRIPTION_LENGTH)
            _description.value = description
    }

    private fun isValidate(): Boolean {
        if(_nameTask.value!!.isBlank()) {
            setErrorMsg("Пустое имя задачи")
            return false
        }
        return true
    }

    private fun save() {
        val task = TaskInfo(
            idTask = 0,
            name = _nameTask.value!!,
            description = _description.value,
            typeLocation = _typeLocationSource.value!!,
            idLocation = _idLocationSource.value,
            dateCreate = System.currentTimeMillis(),
            dateEdit = null,
            dateArchive = null,
            dateStart = null,
            dateEnd = null,
            dateCompleted = null
        )

        viewModelScope.launch {
            val result = saveNewTaskUseCase.execute(task)
            when {
                result.isSuccess -> {
                    val idTask = result.getOrThrow()
                    Log.d("ttt","id idTask = $idTask")
                    //_idNewTask.value = idTask
                    //hideKeyboard()
                    handleBack()
                }
                result.isFailure -> setErrorMsg(result.exceptionOrNull()?.message)
            }
        }
    }
}

/*
@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val saveNewTaskUseCase: SaveNewTaskUseCase
) : ViewModel() {

    private val _idLocationSource = MutableLiveData<Long>()
    val idLocationSource: LiveData<Long> = _idLocationSource

    private val _typeLocationSource = MutableLiveData<TypeLocation>()
    val typeLocationSource: LiveData<TypeLocation> = _typeLocationSource

    private val _backClick = MutableLiveData<Boolean?>(null)
    val backClick: LiveData<Boolean?> = _backClick

    private val _idNewTask = MutableLiveData<Long?>(null)
    val idNewTask: LiveData<Long?> = _idNewTask

    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _nameTask = MutableLiveData<String>("")
    val nameTask: LiveData<String> = _nameTask

    private val _description = MutableLiveData<String>("")
    val description: LiveData<String> = _description

    private lateinit var job: Job


    fun clearScreenState() {
        _idLocationSource.value = 0
        _typeLocationSource.value = TypeLocation.MAIN
        _backClick.value = null
        //_showKeyboard.value = false
        _idNewTask.value = null
        _errorMsg.value = null
        _nameTask.value = ""
        _description.value = ""
        closeStates()
    }

    fun changeNameTask(nameTask: String) {
        if(nameTask.length <= SettingConstant.MAX_TASK_NAME_LENGTH)
            _nameTask.value = nameTask
    }

    fun changeDescription(description: String) {
        if(description.length <= SettingConstant.MAX_TASK_DESCRIPTION_LENGTH)
            _description.value = description
    }

    fun initScreen(screenState: ScreenState) {
        val screen = screenState.currentScreen as? Screen.TaskCreate
        screen?.let {
            _idLocationSource.value = screenState.previousArgs["ID"]?.toLongOrNull()
            _typeLocationSource.value = TypeLocation.getByStr(screenState.previousRawRoute ?: "")
            job = viewModelScope.launch {
                checkAction(screen, this)
            }
        }
    }

    private fun checkAction(screen: Screen.TaskCreate, scope: CoroutineScope) {
        screen.buttons.onEach { button ->
            when(button) {
                TypeAction.ACTION_NAV_BACK -> {
                    handleBack()
                }
                TypeAction.ACTION_SAVE -> {
                    if(isValidate()) {
                        save()
                    }
                }
                else -> Unit
            }
        }.launchIn(scope)
    }

    private fun isValidate(): Boolean {
        if(_nameTask.value!!.isBlank()) {
            _errorMsg.value = "Пустое имя задачи"
            return false
        }
        return true
    }

    private fun save() {
        val task = TaskInfo(
            idTask = 0,
            name = _nameTask.value!!,
            description = _description.value,
            typeLocation = _typeLocationSource.value!!,
            idLocation = _idLocationSource.value,
            dateCreate = System.currentTimeMillis(),
            dateEdit = null,
            dateArchive = null,
            dateStart = null,
            dateEnd = null,
            dateCompleted = null
        )

        viewModelScope.launch {
            val result = saveNewTaskUseCase.execute(task)
            when {
                result.isSuccess -> {
                    val idTask = result.getOrThrow()
                    Log.d("ttt","id idTask = $idTask")
                    //_idNewTask.value = idTask
                    handleBack()
                }
                result.isFailure -> {
                    val e = result.exceptionOrNull()
                    _errorMsg.value = e?.message
                }
            }
        }
    }

    fun clearErrorMsg() { _errorMsg.value = null }
    fun handleBack() { _backClick.value = _backClick.value?.not() ?: true }
    private fun closeStates() { job.cancel() }
}*/
