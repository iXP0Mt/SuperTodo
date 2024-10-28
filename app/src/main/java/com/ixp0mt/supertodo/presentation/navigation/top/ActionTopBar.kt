package com.ixp0mt.supertodo.presentation.navigation.top

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.vector.ImageVector
import com.ixp0mt.supertodo.presentation.util.TypeAction

sealed interface ActionTopBar {
    val title: String
    val icon: ImageVector
    val contentDescription: String?
    val onClick: () -> Unit


    class EditItem(onClick: (typeAction: TypeAction) -> Unit) : ActionTopBar {
        override val title: String = "Edit"
        override val icon: ImageVector = Icons.Default.Edit
        override val contentDescription: String? = null
        override val onClick: () -> Unit = { onClick(TypeAction.ACTION_EDIT) }
    }

    class DeleteItem(onClick: (typeAction: TypeAction) -> Unit) : ActionTopBar {
        override val title: String = "Delete"
        override val icon: ImageVector = Icons.Default.Delete
        override val contentDescription: String? = null
        override val onClick: () -> Unit = { onClick(TypeAction.ACTION_DELETE) }
    }

    class SaveItem(onClick: (typeAction: TypeAction) -> Unit) : ActionTopBar {
        override val title: String = "Save"
        override val icon: ImageVector = Icons.Default.Done
        override val contentDescription: String? = null
        override val onClick: () -> Unit = { onClick(TypeAction.ACTION_SAVE) }
    }
}