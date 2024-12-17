package com.ixp0mt.supertodo.presentation.navigation.bottom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ixp0mt.supertodo.R

class CustomBottomBarViewModel(

) : ViewModel() {

    private val _bottomBarItems = MutableLiveData<List<ActionBottomBar>>(emptyList())
    val bottomBarItems: LiveData<List<ActionBottomBar>> = _bottomBarItems

    private val _navigationRoute = MutableLiveData<String?>(null)
    val navigationRoute: LiveData<String?> = _navigationRoute


    private val _centerButtonIcon = MutableLiveData<Int>(R.drawable.ic_add_element)
    val centerButtonIcon: LiveData<Int> = _centerButtonIcon

    private val _centerButtonState = MutableLiveData<Boolean>(true)


    fun initData() {
        loadNavigationItems()
    }

    private fun loadNavigationItems() {
        if (_centerButtonState.value!!) {
            _centerButtonIcon.value = R.drawable.ic_add_element
            _bottomBarItems.value = ActionBottomBar.itemsForMainPanel
        } else {
            _centerButtonIcon.value = R.drawable.ic_return_bottom_panel
            _bottomBarItems.value = ActionBottomBar.itemsForSecondPanel
        }
    }

    fun onCenterButtonClick() {
        _centerButtonState.value = !_centerButtonState.value!!
        loadNavigationItems()
    }

    fun onItemClick(item: ActionBottomBar) {
        if (item is ActionBottomBar.Navigation) {
            _navigationRoute.value = item.route
        }
    }

    fun resetNavigation() {
        _navigationRoute.value = null
    }
}