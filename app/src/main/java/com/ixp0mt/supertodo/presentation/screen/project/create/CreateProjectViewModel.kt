package com.ixp0mt.supertodo.presentation.screen.project.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.util.SettingConstant
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.usecase.project.SaveNewProjectUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.ElementCreateViewModel
import com.ixp0mt.supertodo.presentation.util.TypeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val saveNewProjectUseCase: SaveNewProjectUseCase
) : ElementCreateViewModel() {

    private val _idNewProject = MutableLiveData<Long?>(null)
    val idNewProject: LiveData<Long?> = _idNewProject

    private val _nameProject = MutableLiveData<String>("")
    val nameProject: LiveData<String> = _nameProject

    private val _description = MutableLiveData<String>("")
    val description: LiveData<String> = _description


    public override fun clearScreenState() {
        super.clearScreenState()
        _idNewProject.value = null
        _nameProject.value = ""
        _description.value = ""
    }

    override fun getScreen(screenState: ScreenState): Screen? =
        screenState.currentScreen as? Screen.ProjectCreate

    override fun checkAction(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.ProjectCreate).buttons.onEach { button ->
            when (button) {
                TypeAction.ACTION_NAV_BACK -> handleBack()
                TypeAction.ACTION_SAVE -> if(isValidate()) save()
                else -> Unit
            }
        }.launchIn(scope)
    }


    fun changeNameProject(nameFolder: String) {
        if(nameFolder.length <= SettingConstant.MAX_PROJECT_NAME_LENGTH)
            _nameProject.value = nameFolder
    }

    fun changeDescription(description: String) {
        if(description.length <= SettingConstant.MAX_PROJECT_DESCRIPTION_LENGTH)
            _description.value = description
    }

    private fun isValidate(): Boolean {
        if(_nameProject.value!!.isBlank()) {
            setErrorMsg("Пустое имя проекта")
            return false
        }
        return true
    }

    private fun save() {
        val project = ProjectInfo(
            idProject = 0,
            name = _nameProject.value!!,
            description = _description.value,
            typeLocation = _typeLocationSource.value!!,
            idLocation = _idLocationSource.value!!,
            dateCreate = System.currentTimeMillis(),
            dateEdit = null,
            dateArchive = null,
            dateStart = null,
            dateEnd = null,
            dateCompleted = null
        )

        viewModelScope.launch {
            val result = saveNewProjectUseCase.execute(project)
            when {
                result.isSuccess -> {
                    val idProject = result.getOrThrow()
                    hideKeyboard()
                    _idNewProject.value = idProject
                }
                result.isFailure -> setErrorMsg(result.exceptionOrNull()?.message)
            }
        }
    }
}
