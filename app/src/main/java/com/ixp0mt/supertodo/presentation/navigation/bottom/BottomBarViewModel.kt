package com.ixp0mt.supertodo.presentation.navigation.bottom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor(

) : ViewModel() {

    private val _bottomBarContain = MutableLiveData<List<ActionBottomBar>>(ActionBottomBar.itemsForMainPanel)
    val bottomBarContain: LiveData<List<ActionBottomBar>> = _bottomBarContain

    private val _navigationRoute = MutableLiveData<String?>(null)
    val navigationRoute: LiveData<String?> = _navigationRoute

    private val _enableBackHandler = MutableLiveData<Boolean>(false)
    val enableBackHandler: LiveData<Boolean> = _enableBackHandler


    fun onButtonClicked(item: ActionBottomBar) {
        when(item) {
            is ActionBottomBar.Navigation -> _navigationRoute.value = item.route
            is ActionBottomBar.ShowSecondPanel -> turnBottomBarPanel(ActionBottomBar.itemsForSecondPanel)
            is ActionBottomBar.ShowMainPanel -> showMainPanel()
            else -> Unit
        }
    }

    fun showMainPanel() {
        turnBottomBarPanel(ActionBottomBar.itemsForMainPanel)
    }

    private fun turnBottomBarPanel(contain: List<ActionBottomBar>) {
        _enableBackHandler.value = !_enableBackHandler.value!!
        _bottomBarContain.value = contain
    }

    fun resetNavigation() {
        _navigationRoute.value = null
    }
}