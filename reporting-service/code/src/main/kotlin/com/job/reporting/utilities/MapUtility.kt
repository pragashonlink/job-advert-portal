package com.job.reporting.utilities

class MapUtility {
    companion object {
        fun convertToMap(any: Any?): Map<String, Any> {
            return when (any) {
                is Map<*, *> -> {
                    @Suppress("UNCHECKED_CAST")
                    any as Map<String, Any>
                }
                null -> {
                    emptyMap()
                }
                else -> {
                    emptyMap()
                }
            }
        }

    }
}