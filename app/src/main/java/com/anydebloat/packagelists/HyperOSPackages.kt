package com.anydebloat.packagelists

import com.anydebloat.models.OEM
import com.anydebloat.models.PackageInfo

object HyperOSPackages {

    private val bloatware = listOf(
        // HyperOS System Apps
        PackageInfo("com.miui.analytics", "HyperOS Analytics", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.bugreport", "Bug Report", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.cloudservice", "Mi Cloud", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.compass", "Compass", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.daemon", "HyperOS Daemon", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.face", "Face Unlock", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.gallery", "Gallery", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.guardprovider", "Guard Provider", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.mediaviewer", "HyperOS Media Viewer", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.mishare.connectivity", "Mi Share", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.mipay", "Mi Pay", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.miwallpaper", "HyperOS Wallpaper", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.newhome", "App Vault", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.notification", "HyperOS Notifications", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.player", "HyperOS Music", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.screenrecorder", "Screen Recorder", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.securitycenter", "Security (HyperOS)", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.securitycore", "Security Core", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.silenceinstaller", "Silent Installer", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.personalassistant", "Personal Assistant", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.voicetrigger", "Voice Trigger", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.weather2", "Weather", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.cleanmaster", "Clean Master", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.yellowpage", "Yellow Pages", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.glowscreenbroadcast", "Glow Screen", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.hyperboost", "HyperBoost", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.aireco", "Air Eco", OEM.HYPEROS, "HyperOS"),
        PackageInfo("com.miui.smartsearch", "Smart Search", OEM.HYPEROS, "HyperOS"),

        // Xiaomi Apps
        PackageInfo("com.xiaomi.account", "Mi Account", OEM.HYPEROS, "Xiaomi"),
        PackageInfo("com.xiaomi.finddevice", "Find Device", OEM.HYPEROS, "Xiaomi"),
        PackageInfo("com.xiaomi.market", "GetApps", OEM.HYPEROS, "Xiaomi"),
        PackageInfo("com.xiaomi.midrop", "Mi Drop", OEM.HYPEROS, "Xiaomi"),
        PackageInfo("com.xiaomi.xmsf", "Mi Push Service", OEM.HYPEROS, "Xiaomi"),
        PackageInfo("com.xiaomi.xiaoailite", "XiaoAI", OEM.HYPEROS, "Xiaomi"),
        PackageInfo("com.xiaomi.gamecenter", "Game Center", OEM.HYPEROS, "Xiaomi"),
        PackageInfo("com.xiaomi.mipicks", "GetApps", OEM.HYPEROS, "Xiaomi"),
        PackageInfo("com.xiaomi.miotapp", "Mi Home", OEM.HYPEROS, "Xiaomi"),
        PackageInfo("com.xiaomi.athena", "Athena", OEM.HYPEROS, "Xiaomi"),

        // Google Apps (HyperOS)
        PackageInfo("com.google.android.googlequicksearchbox", "Google Search", OEM.HYPEROS, "Google"),
        PackageInfo("com.google.android.youtube", "YouTube", OEM.HYPEROS, "Google"),
        PackageInfo("com.google.android.apps.maps", "Google Maps", OEM.HYPEROS, "Google"),
        PackageInfo("com.google.android.gms", "Google Play Services", OEM.HYPEROS, "Google"),
        PackageInfo("com.google.android.gms.location", "Google Location", OEM.HYPEROS, "Google"),
        PackageInfo("com.google.android.tts", "Google Text to Speech", OEM.HYPEROS, "Google"),

        // Operator/Carrier Apps (HyperOS)
        PackageInfo("com.android.chrome", "Chrome", OEM.HYPEROS, "Bloatware"),
        PackageInfo("com.android.systemui", "System UI (Safe to Keep)", OEM.HYPEROS, "System"),
    )

    fun getPackages(): List<PackageInfo> = bloatware
}
