package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.model.element.IElementPlan
import com.ixp0mt.supertodo.domain.usecase.element.CreateElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetLocationAsElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.ValidElementUseCase
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.util.DateTimePickerInfo
import com.ixp0mt.supertodo.presentation.util.KindDateTime
import com.ixp0mt.supertodo.presentation.util.TypeDateTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

open class EditorPlanElementViewModel(

    getElementUseCase: GetElementUseCase,
    validElementUseCase: ValidElementUseCase,
    getLocationAsElementUseCase: GetLocationAsElementUseCase,
    private val createElementUseCase: CreateElementUseCase,
    saveElementUseCase: SaveElementUseCase

) : EditorElementViewModel(

    getElementUseCase = getElementUseCase,
    validElementUseCase = validElementUseCase,
    getLocationAsElementUseCase = getLocationAsElementUseCase,
    createElementUseCase = createElementUseCase,
    saveElementUseCase = saveElementUseCase
) {
    private val _startDateTimePickerInfo = MutableLiveData<DateTimePickerInfo>(DateTimePickerInfo())
    val startDateTimePickerInfo: LiveData<DateTimePickerInfo> = _startDateTimePickerInfo

    private val _endDateTimePickerInfo = MutableLiveData<DateTimePickerInfo>(DateTimePickerInfo())
    val endDateTimePickerInfo: LiveData<DateTimePickerInfo> = _endDateTimePickerInfo

    private val _showStartDateDialog = MutableLiveData<Boolean>(false)
    val showStartDateDialog: LiveData<Boolean> = _showStartDateDialog

    private val _showStartTimeDialog = MutableLiveData<Boolean>(false)
    val showStartTimeDialog: LiveData<Boolean> = _showStartTimeDialog

    private val _showEndDateDialog = MutableLiveData<Boolean>(false)
    val showEndDateDialog: LiveData<Boolean> = _showEndDateDialog

    private val _showEndTimeDialog = MutableLiveData<Boolean>(false)
    val showEndTimeDialog: LiveData<Boolean> = _showEndTimeDialog


    private val _showSelectStartTime = MutableLiveData<Boolean>(false)
    val showSelectStartTime: LiveData<Boolean> = _showSelectStartTime

    private val _showSelectEndTime = MutableLiveData<Boolean>(false)
    val showSelectEndTime: LiveData<Boolean> = _showSelectEndTime



    private val _datePlanStart = MutableLiveData<Long?>(null)
    private val _timePlanStart = MutableLiveData<Long?>(null)

    private val _datePlanEnd = MutableLiveData<Long?>(null)
    private val _timePlanEnd = MutableLiveData<Long?>(null)

    private val _dateFactEnd = MutableLiveData<Long?>(null)



    override suspend fun initData(screenState: ScreenState) {
        super.initData(screenState)

        val currentElement = _currentElement.value as IElementPlan
        val datePlanStart = currentElement.datePlanStart
        val datePlanEnd = currentElement.datePlanEnd

        initPicker(datePlanStart, KindDateTime.START)
        initPicker(datePlanEnd, KindDateTime.END)

        _dateFactEnd.value = currentElement.dateFactEnd
    }

    private fun initPicker(dateTime: Long?, kind: KindDateTime) {
        val resultSplitDateTime = splitDateTime(dateTime)
        val date = resultSplitDateTime.first
        val time = resultSplitDateTime.second

        val resultSplitTime = splitTime(time)
        val hour = resultSplitTime.first
        val minute = resultSplitTime.second

        updatePickerInfo(
            minute = minute,
            hour = hour,
            labelTime = toLabelTime(time),
            labelDate = toLabelDate(date),
            kind = kind
        )

        setDate(date, kind)
        setTime(time, kind)

        if(dateTime != null) showFieldSelectTime(kind)
    }

    private fun toLabelDate(date: Long?): String {
        return if(date != null) {
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(date))
        } else {
            "Не задано"
        }
    }

    private fun toLabelTime(time: Long?): String {
        return if(time != null) {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(time))
        } else ""
    }

    override fun prepareElementToSave(): IElement? {
        val element = super.prepareElementToSave() ?: return null

        val dateTimeStart = if(_datePlanStart.value != null && _timePlanStart.value != null) {
            _datePlanStart.value!! + _timePlanStart.value!!
        } else null

        val dateTimeEnd = if(_datePlanEnd.value != null && _timePlanEnd.value != null) {
            _datePlanEnd.value!! + _timePlanEnd.value!!
        } else null

        return createElementUseCase(
            element.id,
            element.name,
            element.description,
            element.typeLocation,
            element.idLocation,
            element.dateCreate,
            element.dateEdit,
            element.dateArchive,
            dateTimeStart,
            dateTimeEnd,
            _dateFactEnd.value
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun onDatePicker(datePickerState: DatePickerState, kind: KindDateTime) {
        datePickerState.setSelection(getDate(kind))
        showDialog(TypeDateTime.DATE, kind)
    }

    private fun getDate(key: KindDateTime): Long? {
        return when(key) {
            KindDateTime.START -> _datePlanStart.value
            KindDateTime.END -> _datePlanEnd.value
        }
    }


    fun onTimePicker(kind: KindDateTime) {
        showDialog(TypeDateTime.TIME, kind)
    }

    private fun showDialog(typePicker: TypeDateTime, kindPicker: KindDateTime, state: Boolean = true) {
        if(typePicker == TypeDateTime.DATE && kindPicker == KindDateTime.START) _showStartDateDialog.value = state
        else if(typePicker == TypeDateTime.DATE && kindPicker == KindDateTime.END) _showEndDateDialog.value = state
        else if(typePicker == TypeDateTime.TIME && kindPicker == KindDateTime.START) _showStartTimeDialog.value = state
        else if(typePicker == TypeDateTime.TIME && kindPicker == KindDateTime.END) _showEndTimeDialog.value = state
    }

    fun onDateSelected(date: Long?, kind: KindDateTime) {
        if(date == null) return

        setDate(date, kind)

        if(getTime(kind) == null) {
            setTime(28800000, kind) // 8:00
        }

        updatePickerInfo(
            labelDate = toLabelDate(date),
            labelTime = toLabelTime(getTime(kind)),
            kind = kind
        )

        showFieldSelectTime(kind)

        showDialog(TypeDateTime.DATE, kind, false)
    }

    private fun showFieldSelectTime(kind: KindDateTime, state: Boolean = true) {
        when(kind) {
            KindDateTime.START -> _showSelectStartTime.value = state
            KindDateTime.END -> _showSelectEndTime.value = state
        }
    }

    private fun updatePickerInfo(
        minute: Int? = null,
        hour: Int? = null,
        labelDate: String? = null,
        labelTime: String? = null,
        kind: KindDateTime
    ) {
        var tempInfo = when(kind) {
            KindDateTime.START -> _startDateTimePickerInfo.value!!
            KindDateTime.END -> _endDateTimePickerInfo.value!!
        }

        tempInfo = tempInfo.copy(
            minute = minute ?: tempInfo.minute,
            hour = hour ?: tempInfo.hour,
            labelDate = labelDate ?: tempInfo.labelDate,
            labelTime = labelTime ?: tempInfo.labelTime
        )

        when(kind) {
            KindDateTime.START -> _startDateTimePickerInfo.value = tempInfo
            KindDateTime.END -> _endDateTimePickerInfo.value = tempInfo
        }
    }

    private fun setDate(date: Long?, kind: KindDateTime) {
        when(kind) {
            KindDateTime.START -> _datePlanStart.value = date
            KindDateTime.END -> _datePlanEnd.value = date
        }
    }

    private fun getTime(kind: KindDateTime): Long? {
        return when(kind) {
            KindDateTime.START -> _timePlanStart.value
            KindDateTime.END -> _timePlanEnd.value
        }
    }

    private fun setTime(time: Long?, kind: KindDateTime) {
        when(kind) {
            KindDateTime.START -> _timePlanStart.value = time
            KindDateTime.END -> _timePlanEnd.value = time
        }
    }


    fun onTimeSelected(hour: Int, minute: Int, kind: KindDateTime) {
        val timeInMillis = (hour*3600000 + minute*60000).toLong()

        setTime(timeInMillis, kind)

        updatePickerInfo(
            labelTime = toLabelTime(timeInMillis),
            hour = hour,
            minute = minute,
            kind = kind
        )

        showDialog(TypeDateTime.TIME, kind, false)
    }


    fun onDismissDialog() {
        _showStartDateDialog.value = false
        _showStartTimeDialog.value = false
        _showEndTimeDialog.value = false
        _showEndDateDialog.value = false
    }

    private fun splitDateTime(millis: Long?): Pair<Long?, Long?> {
        if(millis == null) return Pair(null,null)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = millis
        }

        val dateCalendar = calendar.clone() as Calendar
        dateCalendar.set(Calendar.HOUR_OF_DAY, 0)
        dateCalendar.set(Calendar.MINUTE, 0)
        dateCalendar.set(Calendar.SECOND, 0)
        dateCalendar.set(Calendar.MILLISECOND, 0)
        val dateInMillis = dateCalendar.timeInMillis

        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        val timeInMillis = (hours * 60 * 60 * 1000L) + (minutes * 60 * 1000L)

        return Pair(dateInMillis, timeInMillis)
    }

    private fun splitTime(millis: Long?): Pair<Int, Int> {
        if(millis == null) return Pair(8,0)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = millis
        }

        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)

        return Pair(hours, minutes)
    }
}