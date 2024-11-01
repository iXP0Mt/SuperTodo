package com.ixp0mt.supertodo.presentation.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class ElementEditViewModel : ViewModel() {
    private val _backClick = MutableLiveData<Boolean?>(null)
    val backClick: LiveData<Boolean?> = _backClick

    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?> = _errorMsg

    protected val _saveEditState = MutableLiveData<Boolean>(false)

    private val _showKeyboard = MutableLiveData<Boolean>(true)
    val showKeyboard: LiveData<Boolean> = _showKeyboard

    private lateinit var job: Job


    protected open fun clearScreenState() {
        _saveEditState.value = false
        _backClick.value = null
        _errorMsg.value = null
        closeStates()
    }

    fun initScreen(screenState: ScreenState) {
        val screen = getScreen(screenState)
        screen?.let {
            val idFolder = getIdElementFromArgs(screenState)
            idFolder?.let {
                job = viewModelScope.launch {
                    initElement(it)
                    checkAction(screen, this)
                }
            }
        }
    }

    protected open fun getScreen(screenState: ScreenState): Screen? = null
    protected open fun checkAction(screen: Screen, scope: CoroutineScope) {}
    protected open fun getIdElementFromArgs(screenState: ScreenState): Long? = null
    protected open suspend fun initElement(idElement: Long) {}

    protected fun setErrorMsg(error: String?) { _errorMsg.value = error }
    protected fun hideKeyboard() { _showKeyboard.value = false }

    fun clearErrorMsg() { _errorMsg.value = null }
    fun handleBack() {
        hideKeyboard()
        _backClick.value = _backClick.value?.not() ?: true
    }
    private fun closeStates() { job.cancel() }
}