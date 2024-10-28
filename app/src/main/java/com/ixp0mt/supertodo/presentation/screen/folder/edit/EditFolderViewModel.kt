package com.ixp0mt.supertodo.presentation.screen.folder.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.util.SettingConstant
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.GetFolderByIdParam
import com.ixp0mt.supertodo.domain.usecase.folder.GetFolderByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.SaveEditFolderUseCase
import com.ixp0mt.supertodo.presentation.navigation.Routes
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.ElementEditViewModel
import com.ixp0mt.supertodo.presentation.util.TypeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditFolderViewModel @Inject constructor(
    private val getFolderByIdUseCase: GetFolderByIdUseCase,
    private val saveEditFolderUseCase: SaveEditFolderUseCase
) : ElementEditViewModel() {
    private val _folderInfo = MutableLiveData<FolderInfo>()

    private val _nameFolder = MutableLiveData<String>("")
    val nameFolder: LiveData<String> = _nameFolder

    private val _description = MutableLiveData<String>("")
    val description: LiveData<String> = _description

    public override fun clearScreenState() {
        super.clearScreenState()
        _nameFolder.value = ""
        _description.value = ""
    }

    fun changeNameFolder(nameFolder: String) {
        if (nameFolder.length <= SettingConstant.MAX_FOLDER_NAME_LENGTH)
            _nameFolder.value = nameFolder
    }

    fun changeDescription(description: String) {
        if (description.length <= SettingConstant.MAX_FOLDER_DESCRIPTION_LENGTH)
            _description.value = description
    }

    override fun getScreen(screenState: ScreenState): Screen? =
        screenState.currentScreen as? Screen.FolderEdit

    override fun getIdElementFromArgs(screenState: ScreenState): Long? =
        screenState.currentArgs[Routes.FolderEdit.ID]?.toLongOrNull()

    override suspend fun initElement(idElement: Long) {
        initFolder(idElement)
    }

    private suspend fun initFolder(idFolder: Long) {
        if (getFolderInfo(idFolder)) {
            _nameFolder.value = _folderInfo.value!!.name
            _description.value = _folderInfo.value?.description ?: ""
        }
    }

    override fun checkAction(screen: Screen, scope: CoroutineScope) {
        (screen as Screen.FolderEdit).buttons.onEach { button ->
            when (button) {
                TypeAction.ACTION_NAV_BACK -> handleBack()
                TypeAction.ACTION_SAVE -> if (isValidate()) saveEdit()
                else -> Unit
            }
        }.launchIn(scope)
    }

    private suspend fun getFolderInfo(idFolder: Long): Boolean {
        val params = GetFolderByIdParam(idFolder)
        val result = getFolderByIdUseCase.execute(params)
        when {
            result.isSuccess -> {
                _folderInfo.value = result.getOrThrow()
                return true
            }

            result.isFailure -> {
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
        if (nameFolder.value!!.isBlank()) {
            setErrorMsg("Пустое имя папки")
            return false
        }
        return true
    }

    private fun saveEdit() {
        val editedFolder = FolderInfo(
            idFolder = _folderInfo.value!!.idFolder,
            name = _nameFolder.value!!,
            description = _description.value,
            typeLocation = _folderInfo.value!!.typeLocation,
            idLocation = _folderInfo.value!!.idLocation,
            dateCreate = _folderInfo.value!!.dateCreate,
            dateEdit = System.currentTimeMillis(),
            dateArchive = _folderInfo.value!!.dateArchive
        )

        viewModelScope.launch {
            val result = saveEditFolderUseCase.execute(editedFolder)
            when {
                result.isSuccess -> {
                    val cho = result.getOrThrow()
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
