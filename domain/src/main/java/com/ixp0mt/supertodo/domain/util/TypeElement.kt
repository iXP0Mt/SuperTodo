package com.ixp0mt.supertodo.domain.util

enum class TypeElement {
    DEFAULT,
    MAIN,
    FOLDER,
    PROJECT,
    TASK;

    companion object {
        fun convert(string: String): TypeElement {
            return TypeElement.entries.find {
                string.contains(it.name, ignoreCase = true)
            } ?: DEFAULT
        }
    }
}