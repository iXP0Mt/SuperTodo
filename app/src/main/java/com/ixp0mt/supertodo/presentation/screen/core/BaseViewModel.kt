package com.ixp0mt.supertodo.presentation.screen.core

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.R
import com.ixp0mt.supertodo.domain.model.ElementInfo
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.GetFolderByIdParam
import com.ixp0mt.supertodo.domain.model.GetProjectByIdParam
import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.model.ValuesElementsInfo
import com.ixp0mt.supertodo.domain.usecase.folder.GetFolderByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersWithCountsSubElementsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsWithCountsSubElementsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTaskByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksWithCountsSubElementsByLocationUseCase
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.presentation.navigation.screen.Screen
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


abstract class BaseViewModel(
    private val getFoldersByLocationUseCase: GetFoldersByLocationUseCase? = null,
    private val getProjectsByLocationUseCase: GetProjectsByLocationUseCase? = null,
    private val getTasksByLocationUseCase: GetTasksByLocationUseCase? = null,
    private val getFolderByIdUseCase: GetFolderByIdUseCase? = null,
    private val getProjectByIdUseCase: GetProjectByIdUseCase? = null,
    private val getTaskByIdUseCase: GetTaskByIdUseCase? = null,
    private val getFoldersWithCountsSubElementsByLocationUseCase: GetFoldersWithCountsSubElementsByLocationUseCase? = null,
    private val getProjectsWithCountsSubElementsByLocationUseCase: GetProjectsWithCountsSubElementsByLocationUseCase? = null,
    private val getTasksWithCountsSubElementsByLocationUseCase: GetTasksWithCountsSubElementsByLocationUseCase? = null
) : ViewModel() {
    protected val _listFolders = MutableLiveData<List<FolderInfo>>(emptyList())
    protected val _listProjects = MutableLiveData<List<ProjectInfo>>(emptyList())
    protected val _listTasks = MutableLiveData<List<TaskInfo>>(emptyList())

    private lateinit var job: Job

    /**
     * Инициализация экрана
     */
    open fun initScreen(screenState: ScreenState) {
        val screen = screenState.currentScreen!!

        job = viewModelScope.launch {
            provideActions(screen, this)
        }
    }

    /**
     * Очистка данных экрана
     */
    open fun clearScreen() {
        job.cancel()
    }

    /**
     * Загрузка содержимого экрана (папки, проекты, задачи), если были правильно переданы UseCase'ы.
     *
     * @param typeLocation Тип элемента, который является локацией для внутренних элементов
     * @param idLocation ID элемента, который является локацией для внутренних элементов
     * @param flags Флаги в двоичном представлении для настройки какие типы внутренних элементов подгружать:
     *              папки (x), проекты (y), задачи (z) - 0bxyz, где вместо символов (x,y,z) надо выставить 0 или 1
     */
    protected suspend fun loadInternalElementsByLocation(typeLocation: TypeLocation, idLocation: Long, flags: Int = 0b111) {
        if((flags and 0b100) != 0) getFoldersByLocationUseCase?.let { loadInternalFolders(typeLocation, idLocation) }
        if((flags and 0b010) != 0) getProjectsByLocationUseCase?.let { loadInternalProjects(typeLocation, idLocation) }
        if((flags and 0b001) != 0) getTasksByLocationUseCase?.let { loadInternalTasks(typeLocation, idLocation) }
    }

    protected suspend fun loadSubElementsWithCountsSubElementsByLocation(typeLocation: TypeLocation, idLocation: Long) {
        getFoldersWithCountsSubElementsByLocationUseCase?.let {
            val listFolders = getSubFoldersWithCountsSubElementsByLocation(typeLocation, idLocation) ?: return
            _listFolders.value = formatCountersListFolders(listFolders)
        }
        getProjectsWithCountsSubElementsByLocationUseCase?.let {
            val listProjects = loadSubProjectsWithCountsSubElementsByLocation(typeLocation, idLocation) ?: return
            _listProjects.value = formatCountersListProjects(listProjects)
        }
        getTasksWithCountsSubElementsByLocationUseCase?.let {
            val listTasks = loadSubTasksWithCountsSubElementsByLocation(typeLocation, idLocation) ?: return
            _listTasks.value = formatCountersListTasks(listTasks)
        }
    }

    /**
     * Загружает внутренние папки элемента, используя тип и ID этого элемента как локацию внутренних папок.
     * Также получая количество внутренних элементов.
     */
    private suspend fun getSubFoldersWithCountsSubElementsByLocation(typeLocation: TypeLocation, idLocation: Long): List<FolderInfo>? {
        val param = LocationParam(typeLocation, idLocation)
        val result = getFoldersWithCountsSubElementsByLocationUseCase!!(param)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }
            result.isFailure -> {

            }
        }
        return null
    }

    /**
     * Загружает внутренние проекты элемента, используя тип и ID этого элемента как локацию внутренних проектов.
     * Также получая количество внутренних элементов.
     */
    private suspend fun loadSubProjectsWithCountsSubElementsByLocation(typeLocation: TypeLocation, idLocation: Long): List<ProjectInfo>? {
        val param = LocationParam(typeLocation, idLocation)
        val result = getProjectsWithCountsSubElementsByLocationUseCase!!(param)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }
            result.isFailure -> {

            }
        }
        return null
    }

    /**
     * Загружает внутренние задачи элемента, используя тип и ID этого элемента как локацию внутренних задач.
     * Также получая количество внутренних элементов.
     */
    private suspend fun loadSubTasksWithCountsSubElementsByLocation(typeLocation: TypeLocation, idLocation: Long): List<TaskInfo>? {
        val param = LocationParam(typeLocation, idLocation)
        val result = getTasksWithCountsSubElementsByLocationUseCase!!(param)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }
            result.isFailure -> {

            }
        }
        return null
    }

    /**
     * Форматирует строку описания как строку со счётчиками подэлементов списка папок.
     *
     * @param list Список папок, который нужно отформатировать
     *
     * @return Форматированный список папок
     */
    private fun formatCountersListFolders(list: List<FolderInfo>): List<FolderInfo> {
        return list.map { element ->
            if(element.countsSubElements != null) {
                element.copy(description = getDeclension(element.countsSubElements!!))
            } else {
                element
            }
        }
    }

    private fun formatCountersListProjects(list: List<ProjectInfo>): List<ProjectInfo> {
        return list.map { element ->
            if(element.countsSubElements != null) {
                element.copy(description = getDeclension(element.countsSubElements!!))
            } else {
                element
            }
        }
    }

    private fun formatCountersListTasks(list: List<TaskInfo>): List<TaskInfo> {
        return list.map { element ->
            if(element.countsSubElements != null) {
                element.copy(description = getDeclension(element.countsSubElements!!))
            } else {
                element
            }
        }
    }


    private fun getDeclension(counters: ValuesElementsInfo): String? {
        fun getWord(number: Int, forms: List<String>): String {
            val n = number % 100
            val lastDigit = n % 10

            return when {
                n in 11..19 -> forms[0]
                lastDigit == 1 -> forms[1]
                lastDigit in 2..4 -> forms[2]
                else -> forms[0]
            }
        }

        val folderDec = listOf("папок", "папка", "папки")
        val projectDec = listOf("проектов", "проект", "проекта")
        val taskDec = listOf("задач", "задача", "задачи")

        val parts = mutableListOf<String>()
        if (counters.folders > 0) {
            parts.add("${counters.folders} ${getWord(counters.folders, folderDec)}")
        }
        if (counters.projects > 0) {
            parts.add("${counters.projects} ${getWord(counters.projects, projectDec)}")
        }
        if (counters.tasks > 0) {
            parts.add("${counters.tasks} ${getWord(counters.tasks, taskDec)}")
        }

        return if(parts.size == 0) null
        else parts.joinToString(" ")
    }


    /**
     * Загрузить внутренние папки данного элемента
     *
     * @param typeLocation Тип элемента, который является локацией для внутренних папок
     * @param idLocation ID элемента
     */
    private suspend fun loadInternalFolders(typeLocation: TypeLocation, idLocation: Long) {
        val param = LocationParam(typeLocation, idLocation)
        val result = getFoldersByLocationUseCase!!(param)
        when {
            result.isSuccess -> {
                _listFolders.value = result.getOrThrow()
            }
            result.isFailure -> {

            }
        }
    }

    /**
     * Загрузить внутренние проекты данного элемента
     *
     * @param typeLocation Тип элемента, который является локацией для внутренних проектов
     * @param idLocation ID элемента
     */
    private suspend fun loadInternalProjects(typeLocation: TypeLocation, idLocation: Long) {
        val param = LocationParam(typeLocation, idLocation)
        val result = getProjectsByLocationUseCase!!(param)
        when {
            result.isSuccess -> {
                _listProjects.value = result.getOrThrow()
            }
            result.isFailure -> {

            }
        }
    }

    /**
     * Загрузить внутренние задачи данного элемента
     *
     * @param typeLocation Тип элемента, который является локацией для внутренних задач
     * @param idLocation ID элемента
     */
    private suspend fun loadInternalTasks(typeLocation: TypeLocation, idLocation: Long) {
        val param = LocationParam(typeLocation, idLocation)
        val result = getTasksByLocationUseCase!!(param)
        when {
            result.isSuccess -> {
                _listTasks.value = result.getOrThrow()
            }
            result.isFailure -> {

            }
        }
    }

    /**
     * Загружает элемент из базы данных по его типу и ID. Загрузить получится только папку, проект или задачу.
     *
     * @param typeElement Тип загружаемого элемента
     * @param idElement ID загружаемого элемента
     */
    protected suspend fun loadElement(typeElement: TypeElement, idElement: Long): ElementInfo? {
        return when (typeElement) {
            TypeElement.FOLDER -> getFolderByIdUseCase?.let { loadFolder(idElement) }
            TypeElement.PROJECT -> getProjectByIdUseCase?.let { loadProject(idElement) }
            TypeElement.TASK -> getTaskByIdUseCase?.let { loadTask(idElement) }
            else -> null
        }
    }

    /**
     * Получает папку по её ID из базы данных
     *
     * @param idElement ID папки в базе данных
     *
     * @return Объект типа FolderInfo если папка успешно получена, иначе null.
     */
    private suspend fun loadFolder(idElement: Long): FolderInfo? {
        val param = GetFolderByIdParam(idElement)
        val result = getFolderByIdUseCase!!(param)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }
            result.isFailure -> {

            }
        }
        return null
    }

    /**
     * Получает проект по его ID из базы данных
     *
     * @param idElement ID проекта в базе данных
     *
     * @return Объект типа ProjectInfo если проект успешно получен, иначе null.
     */
    private suspend fun loadProject(idElement: Long): ProjectInfo? {
        val param = GetProjectByIdParam(idElement)
        val result = getProjectByIdUseCase!!(param)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }
            result.isFailure -> {

            }
        }
        return null
    }

    /**
     * Получает задачу по её ID из базы данных
     *
     * @param idElement ID задачи в базе данных
     *
     * @return Объект типа TaskInfo если задача успешно получена, иначе null.
     */
    private suspend fun loadTask(idElement: Long): TaskInfo? {
        val param = GetTaskByIdParam(idElement)
        val result = getTaskByIdUseCase!!(param)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }
            result.isFailure -> {

            }
        }
        return null
    }


    /**
     * Получить ID иконки элемента-локации по типу локации
     *
     * @param typeLocation Тип локации
     *
     * @return Drawable Int ID иконки
     */
    protected fun getIconIdLocation(typeLocation: TypeLocation): Int {
        return when(typeLocation) {
            TypeLocation.FOLDER -> R.drawable.ic_folder
            TypeLocation.PROJECT -> R.drawable.ic_project
            TypeLocation.TASK -> R.drawable.ic_task
            TypeLocation.MAIN -> R.drawable.ic_home
            else -> R.drawable.baseline_delete_forever_24
        }
    }

    protected abstract fun provideActions(screen: Screen, scope: CoroutineScope)
}