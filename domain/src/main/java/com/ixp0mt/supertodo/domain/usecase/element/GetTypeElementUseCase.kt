package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.element.IElement
import com.ixp0mt.supertodo.domain.model.folder.FolderInfo
import com.ixp0mt.supertodo.domain.model.folder.FolderWithCounters
import com.ixp0mt.supertodo.domain.model.project.ProjectInfo
import com.ixp0mt.supertodo.domain.model.project.ProjectWithCounters
import com.ixp0mt.supertodo.domain.model.task.TaskInfo
import com.ixp0mt.supertodo.domain.model.task.TaskWithCounters
import com.ixp0mt.supertodo.domain.util.TypeElement

class GetTypeElementUseCase {
    operator fun invoke(element: IElement): TypeElement {
        return when(element) {
            is FolderInfo, is FolderWithCounters -> TypeElement.FOLDER
            is ProjectInfo, is ProjectWithCounters -> TypeElement.PROJECT
            is TaskInfo, is TaskWithCounters -> TypeElement.TASK
            else -> TypeElement.DEFAULT
        }
    }
}