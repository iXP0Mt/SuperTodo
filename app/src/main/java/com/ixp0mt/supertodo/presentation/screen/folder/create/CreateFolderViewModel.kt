package com.ixp0mt.supertodo.presentation.screen.folder.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.util.SettingConstant
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.usecase.folder.SaveNewFolderUseCase
import com.ixp0mt.supertodo.presentation.util.TypeAction
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.ElementCreateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateFolderViewModel @Inject constructor(
    private val saveNewFolderUseCase: SaveNewFolderUseCase
) : ElementCreateViewModel() {

    private val _idNewFolder = MutableLiveData<Long?>(null)
    val idNewFolder: LiveData<Long?> = _idNewFolder

    private val _nameFolder = MutableLiveData<String>("")
    val nameFolder: LiveData<String> = _nameFolder

    private val _description = MutableLiveData<String>("")
    val description: LiveData<String> = _description


    public override fun clearScreenState() {
        super.clearScreenState()
        _idNewFolder.value = null
        _nameFolder.value = ""
        _description.value = ""
    }

    override fun getScreen(screenState: ScreenState): Screen? =
        screenState.currentScreen as? Screen.FolderCreate

    override fun checkAction(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.FolderCreate).buttons.onEach { button ->
            when (button) {
                TypeAction.ACTION_NAV_BACK -> handleBack()
                TypeAction.ACTION_SAVE -> if(isValidate()) save()
                else -> Unit
            }
        }.launchIn(scope)
    }


    fun changeNameFolder(nameFolder: String) {
        if(nameFolder.length <= SettingConstant.MAX_PROJECT_NAME_LENGTH)
            _nameFolder.value = nameFolder
    }

    fun changeDescription(description: String) {
        if(description.length <= SettingConstant.MAX_PROJECT_DESCRIPTION_LENGTH)
            _description.value = description
    }

    private fun isValidate(): Boolean {
        if(_nameFolder.value!!.isBlank()) {
            setErrorMsg("Пустое имя папки")
            return false
        }
        return true
    }

    private fun save() {
        val folder = FolderInfo(
            idFolder = 0,
            name = _nameFolder.value!!,
            description = _description.value,
            typeLocation = _typeLocationSource.value!!,
            idLocation = _idLocationSource.value!!,
            dateCreate = System.currentTimeMillis(),
            dateEdit = null,
            dateArchive = null
        )

        viewModelScope.launch {
            val result = saveNewFolderUseCase.execute(folder)
            when {
                result.isSuccess -> {
                    val idFolder = result.getOrThrow()
                    hideKeyboard()
                    _idNewFolder.value = idFolder
                }
                result.isFailure -> setErrorMsg(result.exceptionOrNull()?.message)
            }
        }
    }
}
