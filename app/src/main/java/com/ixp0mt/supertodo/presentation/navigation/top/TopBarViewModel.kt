package com.ixp0mt.supertodo.presentation.navigation.top

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor(

) : ViewModel() {

    private val _listShownItems = MutableLiveData<List<ActionTopBar>>(emptyList())
    val listShownItems: LiveData<List<ActionTopBar>> = _listShownItems

    private val _listOverflowItems = MutableLiveData<List<ActionTopBar>>(emptyList())
    val listOverflowItems: LiveData<List<ActionTopBar>> = _listOverflowItems


    fun initData(screenState: ScreenState) {
        val actions = screenState.actionsTopBar

        _listOverflowItems.value = actions.filter { it.isDropdownItem }
        _listShownItems.value = actions.filter { !it.isDropdownItem }
    }
}