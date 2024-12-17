package com.ixp0mt.supertodo.domain.model.folder

import com.ixp0mt.supertodo.domain.model.element.ICounters
import com.ixp0mt.supertodo.domain.model.element.IElement

class FolderWithCounters(
    folder: FolderInfo,
    override val countSubTasks: Int,
    override val countSubFolders: Int,
    override val countSubProjects: Int
) : IElement by folder, ICounters