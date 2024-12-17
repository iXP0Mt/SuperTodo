package com.ixp0mt.supertodo.domain.model.project

import com.ixp0mt.supertodo.domain.model.element.ICounters
import com.ixp0mt.supertodo.domain.model.element.IElementPlan


class ProjectWithCounters(
    project: ProjectInfo,
    override val countSubTasks: Int,
    override val countSubFolders: Int,
    override val countSubProjects: Int
) : IElementPlan by project, ICounters
