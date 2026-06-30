package com.anydebloat.packagelists

import com.anydebloat.models.OEM
import com.anydebloat.models.PackageInfo

object OnePlusPackages {

    private val bloatware = listOf(
        // OnePlus System Apps
        PackageInfo("com.oneplus.systemui", "OnePlus System UI", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.account", "OnePlus Account", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.launcher", "OnePlus Launcher", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.gesturegestures", "OnePlus Gestures", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.themestore", "OnePlus Theme Store", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.experience", "OnePlus Experience", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.health", "OnePlus Health", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.message", "OnePlus Messages", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.media", "OnePlus Media", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.note", "OnePlus Notes", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.applocker", "OnePlus App Locker", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.filemanager", "OnePlus File Manager", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.newsstand", "OnePlus Newsstand", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.logkit", "OnePlus LogKit", OEM.ONEPLUS, "OxygenOS"),
        PackageInfo("com.oneplus.opush", "OnePlus Push", OEM.ONEPLUS, "OxygenOS"),

        // Google Apps
        PackageInfo("com.google.android.googlequicksearchbox", "Google Search", OEM.ONEPLUS, "Google"),
        PackageInfo("com.google.android.youtube", "YouTube", OEM.ONEPLUS, "Google"),
        PackageInfo("com.google.android.apps.maps", "Google Maps", OEM.ONEPLUS, "Google"),
        PackageInfo("com.google.android.gms", "Google Play Services", OEM.ONEPLUS, "Google"),

        // OnePlus Store
        PackageInfo("com.oneplus.store", "OnePlus Store", OEM.ONEPLUS, "Store"),

        // Generic Bloatware
        PackageInfo("com.facebook.katana", "Facebook", OEM.ONEPLUS, "Bloatware"),
        PackageInfo("com.linkedin.android", "LinkedIn", OEM.ONEPLUS, "Bloatware"),
        PackageInfo("com.instagram.android", "Instagram", OEM.ONEPLUS, "Bloatware"),
    )

    fun getPackages(): List<PackageInfo> = bloatware
}
