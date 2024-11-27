package com.ixp0mt.supertodo.presentation.screen.project.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.util.SettingConstant
import com.ixp0mt.supertodo.domain.model.GetProjectByIdParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.project.SaveEditProjectUseCase
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
class EditProjectViewModel @Inject constructor(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val saveEditProjectUseCase: SaveEditProjectUseCase
) : ElementEditViewModel() {

    private val _projectInfo = MutableLiveData<ProjectInfo>(ProjectInfo.empty())

    private val _nameProject = MutableLiveData<String>("")
    val nameProject: LiveData<String> = _nameProject

    private val _description = MutableLiveData<String>("")
    val description: LiveData<String> = _description

    public override fun clearScreenState() {
        super.clearScreenState()
        _nameProject.value = ""
        _description.value = ""
    }

    fun changeNameProject(nameFolder: String) {
        if (nameFolder.length <= SettingConstant.MAX_FOLDER_NAME_LENGTH)
            _nameProject.value = nameFolder
    }

    fun changeDescription(description: String) {
        if (description.length <= SettingConstant.MAX_FOLDER_DESCRIPTION_LENGTH)
            _description.value = description
    }

    override fun getScreen(screenState: ScreenState): Screen? =
        screenState.currentScreen as? Screen.ProjectEdit

    override fun getIdElementFromArgs(screenState: ScreenState): Long? =
        screenState.currentArgs[Routes.ProjectEdit.ID]?.toLongOrNull()

    override suspend fun initElement(idElement: Long) {
        initProject(idElement)
    }

    private suspend fun initProject(idProject: Long) {
        if (getProjectInfo(idProject)) {
            _nameProject.value = _projectInfo.value!!.name
            _description.value = _projectInfo.value?.description ?: ""
        }
    }

    private suspend fun getProjectInfo(idProject: Long): Boolean {
        val params = GetProjectByIdParam(idProject)
        val result = getProjectByIdUseCase(params)
        when {
            result.isSuccess -> {
                _projectInfo.value = result.getOrThrow()
                return true
            }

            result.isFailure -> {
                val e = result.exceptionOrNull()
                setErrorMsg(e?.message)
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
        if (_nameProject.value!!.isBlank()) {
            setErrorMsg("Пустое имя проекта")
            return false
        }
        return true
    }

    override fun checkAction(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.ProjectEdit).buttons.onEach { button ->
            when (button) {
                TypeAction.ACTION_NAV_BACK -> handleBack()
                TypeAction.ACTION_SAVE -> if (isValidate()) saveEdit()
                else -> Unit
            }
        }.launchIn(scope)
    }

    private fun saveEdit() {
        val editedProject = ProjectInfo(
            idProject = _projectInfo.value!!.idProject,
            name = _nameProject.value!!,
            description = _description.value,
            typeLocation = _projectInfo.value!!.typeLocation,
            idLocation = _projectInfo.value!!.idLocation,
            dateCreate = _projectInfo.value!!.dateCreate,
            dateEdit = System.currentTimeMillis(),
            dateArchive = _projectInfo.value!!.dateArchive,
            dateStart = _projectInfo.value!!.dateStart,
            dateEnd = _projectInfo.value!!.dateEnd,
            dateCompleted = _projectInfo.value!!.dateCompleted
        )

        viewModelScope.launch {
            val result = saveEditProjectUseCase.execute(editedProject)
            when {
                result.isSuccess -> {
                    _saveEditState.value = true
                    handleBack()
                }

                result.isFailure -> {
                    val e = result.exceptionOrNull()
                    setErrorMsg(e?.message)
                }
            }
        }
    }
}

