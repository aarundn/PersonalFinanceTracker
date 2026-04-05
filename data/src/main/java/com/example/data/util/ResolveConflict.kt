package com.example.data.util


fun <E, R> resolveConflicts(
    localData: List<E>,
    remoteData: List<R>,
    remoteMapper: (R) -> E,
    idMapper: (E) -> String,
    idMapperRemote: (R) -> String,
    timeMapper: (E) -> Long,
    timeMapperRemote: (R) -> Long
): List<E> {

    val resolvedData = mutableListOf<E>()
    val localMap = localData.associateBy { idMapper(it) }
    val remoteMap = remoteData.associateBy { idMapperRemote(it) }

   val allIds = (localMap.keys + remoteMap.keys).distinct()

    for (id in allIds) {
        val localDataItem = localMap[id]
        val remoteDataItem = remoteMap[id]

        when {
            localDataItem == null && remoteDataItem != null -> {
                resolvedData.add(remoteMapper(remoteDataItem))
            }
            localDataItem != null && remoteDataItem == null -> {
                resolvedData.add(localDataItem)
            }
            localDataItem != null && remoteDataItem != null -> {
                if (timeMapper(localDataItem) > timeMapperRemote(remoteDataItem)) {
                    resolvedData.add(localDataItem)
                } else {
                    resolvedData.add(remoteMapper(remoteDataItem))
                }
            }
        }
    }

    return resolvedData
}