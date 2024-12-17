package com.ixp0mt.supertodo.domain.usecase.element

import com.ixp0mt.supertodo.domain.util.SettingConstant

abstract class ValidElementUseCase {
    abstract fun validText(str: String): Boolean
    abstract fun validDescription(str: String): Boolean
}

class ValidFolderUseCase : ValidElementUseCase() {
    override fun validText(str: String): Boolean = str.length <= SettingConstant.MAX_FOLDER_NAME_LENGTH
    override fun validDescription(str: String): Boolean = str.length <= SettingConstant.MAX_FOLDER_DESCRIPTION_LENGTH
}

class ValidProjectUseCase : ValidElementUseCase() {
    override fun validText(str: String): Boolean = str.length <= SettingConstant.MAX_PROJECT_NAME_LENGTH
    override fun validDescription(str: String): Boolean = str.length <= SettingConstant.MAX_PROJECT_DESCRIPTION_LENGTH
}

class ValidTaskUseCase : ValidElementUseCase() {
    override fun validText(str: String): Boolean = str.length <= SettingConstant.MAX_TASK_NAME_LENGTH
    override fun validDescription(str: String): Boolean = str.length <= SettingConstant.MAX_TASK_DESCRIPTION_LENGTH
}
