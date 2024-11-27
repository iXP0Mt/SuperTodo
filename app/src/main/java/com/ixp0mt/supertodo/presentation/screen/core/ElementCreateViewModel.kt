package com.ixp0mt.supertodo.presentation.screen.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class ElementCreateViewModel : ViewModel() {

    protected val _idLocationSource = MutableLiveData<Long>(0)
    val idLocationSource: LiveData<Long> = _idLocationSource

    protected val _typeLocationSource = MutableLiveData<TypeLocation>()
    val typeLocationSource: LiveData<TypeLocation> = _typeLocationSource

    private val _backClick = MutableLiveData<Boolean?>(null)
    val backClick: LiveData<Boolean?> = _backClick

    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _showKeyboard = MutableLiveData<Boolean>(true)
    val showKeyboard: LiveData<Boolean> = _showKeyboard

    private lateinit var job: Job


    protected open fun clearScreenState() {
        _idLocationSource.value = 0L
        _typeLocationSource.value = TypeLocation.MAIN
        _backClick.value = null
        _errorMsg.value = null
        _showKeyboard.value = false
        closeStates()
    }

    protected fun setErrorMsg(error: String?) { _errorMsg.value = error }
    protected fun hideKeyboard() { _showKeyboard.value = false }


    fun initScreen(screenState: ScreenState) {
        val screen = getScreen(screenState)
        screen?.let {
            _idLocationSource.value = screenState.previousArgs["ID"]?.toLongOrNull()
            _typeLocationSource.value = TypeLocation.convert(screenState.previousRawRoute ?: "")
            job = viewModelScope.launch {
                checkAction(screen, this)
            }
        }
    }

    protected open fun getScreen(screenState: ScreenState): Screen? = null
    protected open fun checkAction(screen: Screen, scope: CoroutineScope) {}


    fun clearErrorMsg() { _errorMsg.value = null }
    fun handleBack() {
        hideKeyboard()
        _backClick.value = _backClick.value?.not() ?: true
    }
    private fun closeStates() { job.cancel() }
}