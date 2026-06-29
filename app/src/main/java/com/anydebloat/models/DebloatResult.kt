package com.anydebloat.models

data class DebloatResult(
    val packageName: String,
    val success: Boolean,
    val method: DebloatMode,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
