package com.ixp0mt.supertodo.domain.model.task

import com.ixp0mt.supertodo.domain.model.element.ICountersTasks
import com.ixp0mt.supertodo.domain.model.element.IElementPlan


class TaskWithCounters(
    task: TaskInfo,
    override val countSubTasks: Int,
) : IElementPlan by task, ICountersTasks
