package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetListNamesPedigreeUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetSubElementsWithCountersUseCase
import com.ixp0mt.supertodo.domain.usecase.element.MarkElementCompleteUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.util.TypeAction
import kotlinx.coroutines.launch


abstract class EssentialElementViewModel(
    private val getListNamesPedigreeUseCase: GetListNamesPedigreeUseCase,
    private val deleteElementUseCase: DeleteElementUseCase,
    getElementUseCase: GetElementUseCase,
    getSubElementsWithCountersUseCase: GetSubElementsWithCountersUseCase,
    markElementCompleteUseCase: MarkElementCompleteUseCase
) : ElementViewModel(
    getElementUseCase = getElementUseCase,
    getSubElementsWithCountersUseCase = getSubElementsWithCountersUseCase,
    markElementCompleteUseCase = markElementCompleteUseCase
) {
    private val _editClick = MutableLiveData<Boolean?>(null)
    val editClick: LiveData<Boolean?> = _editClick

    private val _showDialogDelete = MutableLiveData<Boolean>(false)
    val showDialogDelete: LiveData<Boolean> = _showDialogDelete

    private val _pedigree = MutableLiveData<String>("")
    val pedigree: LiveData<String> = _pedigree

    override fun clearData() {
        super.clearData()

        _editClick.value = null
        _showDialogDelete.value = false
    }

    override suspend fun initData(screenState: ScreenState) {
        super.initData(screenState)

        _pedigree.value = getStrPedigree(_currentElement.value!! as IElement)
    }

    private suspend fun getStrPedigree(currentElement: IElement): String {
        val listPedigree = getListNamesPedigreeUseCase(currentElement)
        return listPedigree.joinToString(separator = " > ")
    }

    override suspend fun provideActions(button: TypeAction) {
        when(button) {
            TypeAction.ACTION_EDIT -> handleEdit()
            TypeAction.ACTION_DELETE -> handleDelete()
            TypeAction.ACTION_NAV_BACK -> handleBack()
            else -> {}
        }
    }

    fun deleteCurrentElement() {
        if (_showDialogDelete.value == false) return

        cancelDialogDelete()

        val currentElement = _currentElement.value ?: return

        viewModelScope.launch {
            val responseDelete = deleteElementUseCase(currentElement as IElement)

            Log.d("ttt", "numDeletedFolders = ${responseDelete.numDeletedFolders}")
            Log.d("ttt", "numDeletedProjects = ${responseDelete.numDeletedProjects}")
            Log.d("ttt", "numDeletedTasks = ${responseDelete.numDeletedTasks}")

            handleBack()
        }
    }

    private fun handleEdit() {
        _editClick.value = true
    }

    private fun handleDelete() {
        _showDialogDelete.value = true
    }

    fun cancelDialogDelete() {
        _showDialogDelete.value = false
    }
}