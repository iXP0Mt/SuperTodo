package com.ixp0mt.supertodo.domain.util

enum class TypeLocation {
    MAIN,
    FOLDER,
    PROJECT,
    TASK;

    companion object {
        fun convert(str: String): TypeLocation? {
            return entries.find {
                str.contains(it.name, ignoreCase = true)
            }
        }
    }
}