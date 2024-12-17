package com.ixp0mt.supertodo.domain.model.element

interface ICountersTasks {
    val countSubTasks: Int
}

interface ICounters : ICountersTasks {
    val countSubFolders: Int
    val countSubProjects: Int
}
