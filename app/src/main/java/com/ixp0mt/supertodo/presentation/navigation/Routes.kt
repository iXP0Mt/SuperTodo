package com.ixp0mt.supertodo.presentation.navigation


sealed class Routes(
    val rawRoute: String,
    vararg args: String
) {
    val fullRoute: String = if(args.isEmpty()) rawRoute else {
        val builder = StringBuilder(rawRoute)
        args.forEach { builder.append("/{$it}") }
        builder.toString()
    }

    sealed class NoArgumentsRoute(route: String) : Routes(route) {
        operator fun invoke(): String = rawRoute
    }


    data object Loading : NoArgumentsRoute("loading")

    data object MainFolder : NoArgumentsRoute("main")


    data object Folder : Routes("folder", "ID") {
        const val ID = "ID"
        operator fun invoke(idFolder: Long): String = rawRoute.appendArgs(ID to idFolder)
    }

    data object FolderCreate : Routes("folder_create")

    data object FolderEdit : Routes("folder_edit", "ID") {
        const val ID = "ID"
        operator fun invoke(idFolder: Long): String = rawRoute.appendArgs(Folder.ID to idFolder)
    }


    data object Project : Routes("project", "ID") {
        const val ID = "ID"
        operator fun invoke(idProject: Long): String = rawRoute.appendArgs(ID to idProject)
    }

    data object ProjectCreate : Routes("project_create")

    data object ProjectEdit : Routes("project_edit", "ID") {
        const val ID = "ID"
        operator fun invoke(idProject: Long): String = rawRoute.appendArgs(Folder.ID to idProject)
    }


    data object Task : Routes("task", "ID") {
        const val ID = "ID"
        operator fun invoke(idTask: Long): String = rawRoute.appendArgs(ID to idTask)
    }

    data object TaskCreate : Routes("task_create")

    data object TaskEdit : Routes("task_edit", "ID") {
        const val ID = "ID"
        operator fun invoke(idTask: Long): String = rawRoute.appendArgs(Folder.ID to idTask)
    }
}

internal fun String.appendArgs(vararg args: Pair<String, Any?>): String {
    val builder = StringBuilder(this)
    args.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }
    return builder.toString()
}



