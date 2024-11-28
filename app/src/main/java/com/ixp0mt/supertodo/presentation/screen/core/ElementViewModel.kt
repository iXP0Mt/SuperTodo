package com.ixp0mt.supertodo.presentation.screen.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ixp0mt.supertodo.domain.model.ElementInfo
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.usecase.element.DeleteElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetNamesFullLocationElementUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFolderByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.folder.GetFoldersByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.project.GetProjectsByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.project.MarkCompleteProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTaskByIdUseCase
import com.ixp0mt.supertodo.domain.usecase.task.GetTasksByLocationUseCase
import com.ixp0mt.supertodo.domain.usecase.task.MarkCompleteTaskUseCase
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.util.TypeAction
import kotlinx.coroutines.launch

abstract class ElementViewModel(
    getFoldersByLocationUseCase: GetFoldersByLocationUseCase? = null,
    getProjectsByLocationUseCase: GetProjectsByLocationUseCase? = null,
    getTasksByLocationUseCase: GetTasksByLocationUseCase? = null,
    getFolderByIdUseCase: GetFolderByIdUseCase? = null,
    getProjectByIdUseCase: GetProjectByIdUseCase? = null,
    getTaskByIdUseCase: GetTaskByIdUseCase? = null,
    private val markCompleteProjectUseCase: MarkCompleteProjectUseCase? = null,
    private val markCompleteTaskUseCase: MarkCompleteTaskUseCase,
    private val getNamesFullLocationElementUseCase: GetNamesFullLocationElementUseCase? = null,
    private val deleteElementUseCase: DeleteElementUseCase? = null
) : BaseViewModel(
    getFoldersByLocationUseCase = getFoldersByLocationUseCase,
    getProjectsByLocationUseCase = getProjectsByLocationUseCase,
    getTasksByLocationUseCase = getTasksByLocationUseCase,
    getFolderByIdUseCase = getFolderByIdUseCase,
    getProjectByIdUseCase = getProjectByIdUseCase,
    getTaskByIdUseCase = getTaskByIdUseCase
) {
    private val _elementInfo = MutableLiveData<ElementInfo?>(null)
    val elementInfo: LiveData<ElementInfo?> = _elementInfo

    private val _pedigree = MutableLiveData<String>("")
    val pedigree: LiveData<String> = _pedigree

    val listFolders: LiveData<List<FolderInfo>> = _listFolders
    val listProjects: LiveData<List<ProjectInfo>> = _listProjects
    val listTasks: LiveData<List<TaskInfo>> = _listTasks

    private val _backClick = MutableLiveData<Boolean?>(null)
    val backClick: LiveData<Boolean?> = _backClick

    private val _editClick = MutableLiveData<Boolean?>(null)
    val editClick: LiveData<Boolean?> = _editClick

    private val _internalElementClickInfo = MutableLiveData<ElementParam?>(null)
    val internalElementClickInfo: LiveData<ElementParam?> = _internalElementClickInfo

    private val _showDialogDelete = MutableLiveData<Boolean>(false)
    val showDialogDelete: LiveData<Boolean> = _showDialogDelete

    override fun initScreen(screenState: ScreenState) {
        super.initScreen(screenState)

        initElement(screenState)
    }

    override fun clearScreen() {
        super.clearScreen()

        _backClick.value = null
        _editClick.value = null
        _internalElementClickInfo.value = null
        _showDialogDelete.value = false
    }

    /**
     * Инициализировать текущий элемент, будь то папка, проект или задача
     */
    protected open fun initElement(screenState: ScreenState) {
        val idElement = screenState.currentArgs["ID"]?.toLongOrNull()
        if (idElement === null) return

        val route = screenState.currentScreen?.route?.rawRoute
        if (route === null) return

        val typeElement = TypeElement.convert(route)
        if (typeElement === null) return

        viewModelScope.launch {
            val element = loadElement(typeElement, idElement)
            if (element === null) return@launch

            setFormattedPedigree(element.typeLocation, element.idLocation)

            loadInternalElements(typeElement, idElement)
            _elementInfo.value = element
        }
    }

    /**
     * Обработка действий TopBar
     */
    protected fun handleAction(button: TypeAction) {
        when (button) {
            TypeAction.ACTION_NAV_BACK -> handleBack()
            TypeAction.ACTION_EDIT -> handleEdit()
            TypeAction.ACTION_DELETE -> handleDelete()
            else -> Unit
        }
    }


    /**
     * Отметить элемент как завершенный
     */
    fun markCompleteElement(element: ElementInfo) {
        viewModelScope.launch {
            val dateComplete = when (element) {
                is TaskInfo -> markCompleteTask(element)
                is ProjectInfo -> if(markCompleteProjectUseCase == null) -1 else markCompleteProject(element)
                else -> return@launch
            }

            if (dateComplete?.toInt() == -1) return@launch
            when (element) {
                is TaskInfo -> updateListTasksWithMark(element, dateComplete)
                is ProjectInfo -> updateListProjectsWithMark(element, dateComplete)
            }
        }
    }

    /**
     * Отмечает задачу как выполненную или, наоборот, снимает отметку.
     *
     * @param task Задача
     *
     * @return Дату завершения задачи в виде Long. Если была снята отметка, то null. Иначе -1.
     */
    private suspend fun markCompleteTask(task: TaskInfo): Long? {
        val result = markCompleteTaskUseCase(task)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }

            result.isFailure -> {
            }
        }
        return -1
    }

    /**
     * Обновляет список задач, выставляя отметку данной задаче или, наоборот, снимая её.
     *
     * @param task Целевая задача
     * @param dateComplete Дата выполнения задачи или null
     */
    private fun updateListTasksWithMark(task: TaskInfo, dateComplete: Long?) {
        val updatedListTasks = _listTasks.value?.map { lTask ->
            if (lTask.idTask == task.idTask) {
                lTask.copy(dateCompleted = dateComplete)
            } else {
                lTask
            }
        } ?: emptyList()
        _listTasks.value = updatedListTasks
    }

    /**
     * Отмечает проект как выполненный или, наоборот, снимает отметку.
     *
     * @param project Прроект
     *
     * @return Дату завершения проекта в виде Long. Если была снята отметка, то null. Иначе -1.
     */
    private suspend fun markCompleteProject(project: ProjectInfo): Long? {
        val result = markCompleteProjectUseCase!!(project)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }

            result.isFailure -> {
            }
        }
        return -1
    }

    /**
     * Обновляет список проектов, выставляя отметку данному проекту или, наоборот, снимая её.
     *
     * @param project Целевой проект
     * @param dateComplete Дата выполнения проекта или null
     */
    private fun updateListProjectsWithMark(project: ProjectInfo, dateComplete: Long?) {
        val updatedListProjects = _listProjects.value?.map { lProject ->
            if (lProject.idProject == project.idProject) {
                lProject.copy(dateCompleted = dateComplete)
            } else {
                lProject
            }
        } ?: emptyList()
        _listProjects.value = updatedListProjects
    }

    /**
     * Удалить текущий элемент
     */
    fun deleteCurrentElement() {
        if (_showDialogDelete.value == false) return

        cancelDialogDelete()

        if (_elementInfo.value == null) return

        if(deleteElementUseCase == null) return

        viewModelScope.launch {
            if(deleteElement(_elementInfo.value!!))
                handleBack()
        }
    }

    /**
     * Удаляет элемент (папку, проект, задачу) из базы данных
     *
     * @param element Целевой элемент для удаления
     *
     * @return true если элемент успешно удалён, иначе false
     */
    private suspend fun deleteElement(element: ElementInfo): Boolean {
        val result = deleteElementUseCase!!(element)
        when {
            result.isSuccess -> {
                val responseDelete = result.getOrThrow()
                Log.d("ttt", "numDeletedFolders = ${responseDelete.numDeletedFolders}")
                Log.d("ttt", "numDeletedProjects = ${responseDelete.numDeletedProjects}")
                Log.d("ttt", "numDeletedTasks = ${responseDelete.numDeletedTasks}")
                return true
            }

            result.isFailure -> {
            }
        }
        return false
    }


    /**
     * Обработка действия "назад" из TopBar
     */
    fun handleBack() {
        _backClick.value = _backClick.value?.not() ?: true
    }

    /**
     * Обработка действия "редактировать" из TopBar
     */
    private fun handleEdit() {
        _editClick.value = true
    }

    /**
     * Обработка действия "удалить" из TopBar
     */
    private fun handleDelete() {
        _showDialogDelete.value = true
    }

    /**
     * Закрыть окно удаления
     */
    fun cancelDialogDelete() {
        _showDialogDelete.value = false
    }

    /**
     * Обработка клика по внутреннему элементу
     */
    fun elementClick(typeElement: TypeElement, idElement: Long) {
        if (_internalElementClickInfo.value == null) {
            _internalElementClickInfo.value = ElementParam(typeElement, idElement)
        }
    }

    /**
     * Получить иерархический список названий элементов расположения данного элемента
     */
    private suspend fun getPedigreeLocations(typeLocation: TypeLocation, idLocation: Long): List<String> {
        val param = LocationParam(typeLocation, idLocation)
        val result = getNamesFullLocationElementUseCase!!(param)
        when {
            result.isSuccess -> {
                return result.getOrThrow()
            }

            result.isFailure -> {
            }
        }
        return emptyList()
    }

    /**
     * Превратить список локаций в образцовую строку
     */
    private suspend fun setFormattedPedigree(typeLocation: TypeLocation, idLocation: Long) {
        if(getNamesFullLocationElementUseCase == null) return

        val locations = getPedigreeLocations(typeLocation, idLocation)
        _pedigree.value = locations.joinToString(separator = " > ")
    }
}