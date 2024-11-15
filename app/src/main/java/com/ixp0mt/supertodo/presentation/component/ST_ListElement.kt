package com.ixp0mt.supertodo.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ixp0mt.supertodo.domain.model.ElementInfo
import com.ixp0mt.supertodo.domain.model.FolderInfo
import com.ixp0mt.supertodo.domain.model.ProjectInfo
import com.ixp0mt.supertodo.domain.model.TaskInfo
import com.ixp0mt.supertodo.domain.util.TypeElement

@Composable
fun ST_ListElement(
    listFolders: List<FolderInfo> = emptyList(),
    listProjects: List<ProjectInfo> = emptyList(),
    listTasks: List<TaskInfo> = emptyList(),
    onClickMain: (ElementInfo) -> Unit,
    onClickExtend: (TypeElement, Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(listFolders) {
            ST_FolderCard(
                item = it,
                onClick = { onClickExtend(TypeElement.FOLDER, it.idFolder) }
            )
        }
        items(listProjects) {
            ST_ProjectCard(
                item = it,
                onClickMain = { onClickMain(it) },
                onClickExtend = { onClickExtend(TypeElement.PROJECT, it.idProject) }
            )
        }
        items(listTasks) {
            ST_TaskCard(
                item = it,
                onClickMain = { onClickMain(it) },
                onClickExtend = { onClickExtend(TypeElement.TASK, it.idTask) }
            )
        }
    }
}