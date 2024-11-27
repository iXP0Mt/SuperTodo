package com.ixp0mt.supertodo.presentation.screen.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.model.ElementInfo
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.GetFolderByIdParam
import com.ixp0mt.supertodo.domain.model.GetProjectByIdParam
import com.ixp0mt.supertodo.domain.model.GetTaskByIdParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.usecase.folder.GetFolderByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTaskByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
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
    private val getTaskByIdUseCase: GetTaskByIdUseCase? = null
) : ViewModel() {
    protected val _listFolders = MutableLiveData<List<FolderInfo>>(emptyList())
    protected val _listProjects = MutableLiveData<List<ProjectInfo>>(emptyList())
    protected val _listTasks = MutableLiveData<List<TaskInfo>>(emptyList())

    private lateinit var job: Job

    /**
     * Инициализация экрана
     */
    open fun initScreen(screenState: ScreenState) {
        val screen = provideScreen(screenState)

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
     * Загрузка содержимого экрана (папки, проекты, задачи), если были правильно переданы UseCase'ы
     *
     * @param typeElement Для какого типа элемента загружать содержимое
     * @param idElement Для какого ID элемента загружать содержимое
     */
    protected suspend fun loadInternalElements(typeElement: TypeElement, idElement: Long) {
        getFoldersByLocationUseCase?.let { loadInternalFolders(typeElement, idElement) }
        getProjectsByLocationUseCase?.let { loadInternalProjects(typeElement, idElement) }
        getTasksByLocationUseCase?.let { loadInternalTasks(typeElement, idElement) }
    }


    /**
     * Загрузить внутренние папки данного элемента
     *
     * @param typeElement Тип элемента, у которого будут получены папки, которые он содержит
     * @param idElement ID элемента
     */
    private suspend fun loadInternalFolders(typeElement: TypeElement, idElement: Long) {
        val param = LocationParam(TypeLocation.convert(typeElement.name), idElement)
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
     * @param typeElement Тип элемента, у которого будут получены проекты, которые он содержит
     * @param idElement ID элемента
     */
    private suspend fun loadInternalProjects(typeElement: TypeElement, idElement: Long) {
        val param = LocationParam(TypeLocation.convert(typeElement.name), idElement)
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
     * @param typeElement Тип элемента, у которого будут получены задачи, которые он содержит
     * @param idElement ID элемента
     */
    private suspend fun loadInternalTasks(typeElement: TypeElement, idElement: Long) {
        val param = LocationParam(TypeLocation.convert(typeElement.name), idElement)
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
     * Перейти на страницу отображения данного элемента
     */
    fun handleElement() {

    }

    /**
     * Предоставляет экран для базового класса ViewModel
     */
    protected abstract fun provideScreen(screenState: ScreenState): Screen

    //protected abstract fun provideElement()

    protected abstract fun provideActions(screen: Screen, scope: CoroutineScope)
}