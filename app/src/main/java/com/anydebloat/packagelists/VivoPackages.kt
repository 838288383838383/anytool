package com.anydebloat.packagelists

import com.anydebloat.models.OEM
import com.anydebloat.models.PackageInfo

object VivoPackages {

    private val bloatware = listOf(
        // Vivo FunTouchOS System Apps
        PackageInfo("com.vivo.systemui", "FunTouchOS System UI", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.account", "Vivo Account", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.launcher", "FunTouchOS Launcher", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.appstore", "Vivo App Store", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.store", "Vivo Store", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.music", "Vivo Music", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.gallery", "Vivo Gallery", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.browser", "Vivo Browser", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.camera", "FunTouchOS Camera", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.notes", "Vivo Notes", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.filemanager", "Vivo File Manager", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.safecenter", "Vivo Safe Center", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.theme", "FunTouchOS Theme Store", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.themecenter", "Vivo Theme Center", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.weather", "Vivo Weather", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.android.weather", "System Weather", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.notification", "FunTouchOS Notifications", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.assistant", "Vivo Assistant", OEM.VIVO, "FunTouchOS"),
        PackageInfo("com.vivo.gamestate", "Vivo Game State", OEM.VIVO, "FunTouchOS"),

        // Google Apps
        PackageInfo("com.google.android.googlequicksearchbox", "Google Search", OEM.VIVO, "Google"),
        PackageInfo("com.google.android.youtube", "YouTube", OEM.VIVO, "Google"),
        PackageInfo("com.google.android.apps.maps", "Google Maps", OEM.VIVO, "Google"),
        PackageInfo("com.google.android.gms", "Google Play Services", OEM.VIVO, "Google"),

        // Third-party Bloatware
        PackageInfo("com.facebook.katana", "Facebook", OEM.VIVO, "Bloatware"),
        PackageInfo("com.tencent.mm", "WeChat", OEM.VIVO, "Bloatware"),
        PackageInfo("com.qiyi.video", "iQiyi", OEM.VIVO, "Bloatware"),
    )

    fun getPackages(): List<PackageInfo> = bloatware
}
