package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.model.element.ICounters
import com.ixp0mt.supertodo.domain.model.element.ICountersTasks
import com.ixp0mt.supertodo.domain.model.element.IElement

class GetStrCountersUseCase {
    operator fun invoke(element: IElement): String? {
        return when(element) {
            is ICounters -> {
                getDeclension(
                    countFolders = element.countSubFolders,
                    countProjects =  element.countSubProjects,
                    countTasks =  element.countSubTasks
                )
            }
            is ICountersTasks -> {
                getDeclension(
                    countTasks = element.countSubTasks
                )
            }
            else -> null
        }
    }

    private fun getDeclension(countFolders: Int = 0, countProjects: Int = 0, countTasks: Int): String? {
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
        if (countFolders > 0) {
            parts.add("$countFolders ${getWord(countFolders, folderDec)}")
        }
        if (countProjects > 0) {
            parts.add("$countProjects ${getWord(countProjects, projectDec)}")
        }
        if (countTasks > 0) {
            parts.add("$countTasks ${getWord(countTasks, taskDec)}")
        }

        return if(parts.size == 0) null
        else parts.joinToString(" ")
    }
}


