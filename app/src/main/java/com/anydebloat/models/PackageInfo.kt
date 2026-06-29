package com.anydebloat.models

data class PackageInfo(
    val packageName: String,
    val displayName: String,
    val oem: OEM,
    val category: String,
    val isSystem: Boolean = true,
    val isRemovable: Boolean = true,
    var isSelected: Boolean = false,
    var isEnabled: Boolean = true,
    var isInstalled: Boolean = true
)
