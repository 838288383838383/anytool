package com.anydebloat.packagelists

import com.anydebloat.models.OEM
import com.anydebloat.models.PackageInfo

object OppoPackages {

    private val bloatware = listOf(
        // Oppo ColorOS System Apps
        PackageInfo("com.oppo.systemui", "ColorOS System UI", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.account", "Oppo Account", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.launcher", "ColorOS Launcher", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.appstore", "Oppo App Store", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.store", "Oppo Store", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.music", "Oppo Music", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.gallery", "Oppo Gallery", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.browser", "Oppo Browser", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.camera", "ColorOS Camera", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.notes", "Oppo Notes", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.filemanager", "Oppo File Manager", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.safecenter", "Oppo Safe Center", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.theme", "ColorOS Theme Store", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.usercenter", "Oppo User Center", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.logkit", "Oppo LogKit", OEM.OPPO, "ColorOS"),
        PackageInfo("com.oppo.colorOS.notification", "ColorOS Notifications", OEM.OPPO, "ColorOS"),
        PackageInfo("com.heytap.speechassist", "Heytap Assistant", OEM.OPPO, "ColorOS"),
        PackageInfo("com.heytap.contacts", "Heytap Contacts", OEM.OPPO, "ColorOS"),

        // Google Apps
        PackageInfo("com.google.android.googlequicksearchbox", "Google Search", OEM.OPPO, "Google"),
        PackageInfo("com.google.android.youtube", "YouTube", OEM.OPPO, "Google"),
        PackageInfo("com.google.android.apps.maps", "Google Maps", OEM.OPPO, "Google"),
        PackageInfo("com.google.android.gms", "Google Play Services", OEM.OPPO, "Google"),

        // Third-party Bloatware
        PackageInfo("com.facebook.katana", "Facebook", OEM.OPPO, "Bloatware"),
        PackageInfo("com.tencent.mm", "WeChat", OEM.OPPO, "Bloatware"),
        PackageInfo("com.alibaba.android.rimet", "DingTalk", OEM.OPPO, "Bloatware"),
    )

    fun getPackages(): List<PackageInfo> = bloatware
}
