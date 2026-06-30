package com.anydebloat.packagelists

import com.anydebloat.models.OEM
import com.anydebloat.models.PackageInfo

object RealmePackages {

    private val bloatware = listOf(
        // Realme System Apps
        PackageInfo("com.realme.systemui", "Realme System UI", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.account", "Realme Account", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.launcher", "Realme Launcher", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.appstore", "Realme App Store", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.store", "Realme Store", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.music", "Realme Music", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.gallery", "Realme Gallery", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.browser", "Realme Browser", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.camera", "Realme Camera", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.notes", "Realme Notes", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.filemanager", "Realme File Manager", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.securecenter", "Realme Secure Center", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.theme", "Realme Theme Store", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.weather", "Realme Weather", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.reasearch", "Realme Research", OEM.REALME, "Realme UI"),
        PackageInfo("com.realme.opush", "Realme Push Service", OEM.REALME, "Realme UI"),

        // Google Apps
        PackageInfo("com.google.android.googlequicksearchbox", "Google Search", OEM.REALME, "Google"),
        PackageInfo("com.google.android.youtube", "YouTube", OEM.REALME, "Google"),
        PackageInfo("com.google.android.apps.maps", "Google Maps", OEM.REALME, "Google"),
        PackageInfo("com.google.android.gms", "Google Play Services", OEM.REALME, "Google"),

        // Third-party Bloatware
        PackageInfo("com.facebook.katana", "Facebook", OEM.REALME, "Bloatware"),
        PackageInfo("com.tencent.mm", "WeChat", OEM.REALME, "Bloatware"),
        PackageInfo("com.tiktok.android", "TikTok", OEM.REALME, "Bloatware"),
    )

    fun getPackages(): List<PackageInfo> = bloatware
}
