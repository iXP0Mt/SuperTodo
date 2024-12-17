package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.R
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.util.TypeAction
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _backClick = MutableLiveData<Boolean?>(null)
    val backClick: LiveData<Boolean?> = _backClick

    private lateinit var job: Job


    fun clearBase() {
        _backClick.value = null
        job.cancel()

        clearData()
    }

    fun initBase(screenState: ScreenState) {
        job = viewModelScope.launch {
            screenState.currentScreen.buttons.onEach { action ->
                provideActions(action)
            }.launchIn(this)
        }

        viewModelScope.launch {
            initData(screenState)
        }
    }

    open fun handleBack() {
        _backClick.value = _backClick.value?.not() ?: true
    }

    protected fun getIconIdElement(typeElement: TypeElement): Int {
        return when(typeElement) {
            TypeElement.MAIN -> R.drawable.ic_home
            TypeElement.DEFAULT -> R.drawable.ic_default
            TypeElement.FOLDER -> R.drawable.ic_folder
            TypeElement.PROJECT -> R.drawable.ic_project
            TypeElement.TASK -> R.drawable.ic_task
        }
    }

    protected abstract suspend fun provideActions(button: TypeAction)

    protected abstract fun clearData()

    protected abstract suspend fun initData(screenState: ScreenState)
}