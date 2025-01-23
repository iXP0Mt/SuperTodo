package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import com.ixp0mt.supertodo.domain.usecase.element.CreateFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.CreateProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.element.CreateTaskUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetLocationAsElementUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.element.GetTaskUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveEditFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveEditProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveEditTaskUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveNewFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveNewProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.element.SaveNewTaskUseCase
import com.ixp0mt.supertodo.domain.usecase.element.ValidFolderUseCase
import com.ixp0mt.supertodo.domain.usecase.element.ValidProjectUseCase
import com.ixp0mt.supertodo.domain.usecase.element.ValidTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorNewFolderViewModel @Inject constructor(
    getFolderUseCase: GetFolderUseCase,
    validFolderUseCase: ValidFolderUseCase,
    getLocationAsElementUseCase: GetLocationAsElementUseCase,
    createFolderUseCase: CreateFolderUseCase,
    saveNewFolderUseCase: SaveNewFolderUseCase
) : EditorElementViewModel(
    getElementUseCase = getFolderUseCase,
    validElementUseCase = validFolderUseCase,
    getLocationAsElementUseCase = getLocationAsElementUseCase,
    createElementUseCase = createFolderUseCase,
    saveElementUseCase = saveNewFolderUseCase
)

@HiltViewModel
class EditorEditFolderViewModel @Inject constructor(
    getFolderUseCase: GetFolderUseCase,
    validFolderUseCase: ValidFolderUseCase,
    getLocationAsElementUseCase: GetLocationAsElementUseCase,
    createFolderUseCase: CreateFolderUseCase,
    saveEditFolderUseCase: SaveEditFolderUseCase
) : EditorElementViewModel(
    getElementUseCase = getFolderUseCase,
    validElementUseCase = validFolderUseCase,
    getLocationAsElementUseCase = getLocationAsElementUseCase,
    createElementUseCase = createFolderUseCase,
    saveElementUseCase = saveEditFolderUseCase
)

@HiltViewModel
class EditorNewProjectViewModel @Inject constructor(
    getProjectUseCase: GetProjectUseCase,
    validProjectUseCase: ValidProjectUseCase,
    getLocationAsElementUseCase: GetLocationAsElementUseCase,
    createProjectUseCase: CreateProjectUseCase,
    saveNewProjectUseCase: SaveNewProjectUseCase
) : EditorPlanElementViewModel(
    getElementUseCase = getProjectUseCase,
    validElementUseCase = validProjectUseCase,
    getLocationAsElementUseCase = getLocationAsElementUseCase,
    createElementUseCase = createProjectUseCase,
    saveElementUseCase = saveNewProjectUseCase
)

@HiltViewModel
class EditorEditProjectViewModel @Inject constructor(
    getProjectUseCase: GetProjectUseCase,
    validProjectUseCase: ValidProjectUseCase,
    getLocationAsElementUseCase: GetLocationAsElementUseCase,
    createProjectUseCase: CreateProjectUseCase,
    saveEditProjectUseCase: SaveEditProjectUseCase
) : EditorPlanElementViewModel(
    getElementUseCase = getProjectUseCase,
    validElementUseCase = validProjectUseCase,
    getLocationAsElementUseCase = getLocationAsElementUseCase,
    createElementUseCase = createProjectUseCase,
    saveElementUseCase = saveEditProjectUseCase
)

@HiltViewModel
class EditorNewTaskViewModel @Inject constructor(
    getTaskUseCase: GetTaskUseCase,
    validTaskUseCase: ValidTaskUseCase,
    getLocationAsElementUseCase: GetLocationAsElementUseCase,
    createTaskUseCase: CreateTaskUseCase,
    saveNewTaskUseCase: SaveNewTaskUseCase
) : EditorPlanElementViewModel(
    getElementUseCase = getTaskUseCase,
    validElementUseCase = validTaskUseCase,
    getLocationAsElementUseCase = getLocationAsElementUseCase,
    createElementUseCase = createTaskUseCase,
    saveElementUseCase = saveNewTaskUseCase
)

@HiltViewModel
class EditorEditTaskViewModel @Inject constructor(
    getTaskUseCase: GetTaskUseCase,
    validTaskUseCase: ValidTaskUseCase,
    getLocationAsElementUseCase: GetLocationAsElementUseCase,
    createTaskUseCase: CreateTaskUseCase,
    saveEditTaskUseCase: SaveEditTaskUseCase
) : EditorPlanElementViewModel(
    getElementUseCase = getTaskUseCase,
    validElementUseCase = validTaskUseCase,
    getLocationAsElementUseCase = getLocationAsElementUseCase,
    createElementUseCase = createTaskUseCase,
    saveElementUseCase = saveEditTaskUseCase
)