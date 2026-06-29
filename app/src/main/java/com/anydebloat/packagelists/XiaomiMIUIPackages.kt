package com.anydebloat.packagelists

import com.anydebloat.models.OEM
import com.anydebloat.models.PackageInfo

object XiaomiMIUIPackages {

    private val bloatware = listOf(
        // MIUI System Apps
        PackageInfo("com.miui.analytics", "MIUI Analytics", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.bugreport", "Bug Report", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.cloudservice", "Mi Cloud", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.compass", "Compass", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.daemon", "MIUI Daemon", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.face", "Face Unlock", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.gallery", "Gallery", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.guardprovider", "Guard Provider", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.mediaviewer", "MIUI Media Viewer", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.mishare.connectivity", "Mi Share", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.mipay", "Mi Pay", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.miwallpaper", "MIUI Wallpaper", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.newhome", "App Vault", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.notification", "MIUI Notifications", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.player", "MIUI Music", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.screenrecorder", "Screen Recorder", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.securitycenter", "Security", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.securitycore", "Security Core", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.silenceinstaller", "Silent Installer", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.personalassistant", "Personal Assistant", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.tsmclient", "TSM Client", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.voicetrigger", "Voice Trigger", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.weather2", "Weather", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.cleanmaster", "Clean Master", OEM.XIAOMI, "MIUI"),
        PackageInfo("com.miui.yellowpage", "Yellow Pages", OEM.XIAOMI, "MIUI"),

        // Xiaomi Apps
        PackageInfo("com.xiaomi.account", "Mi Account", OEM.XIAOMI, "Xiaomi"),
        PackageInfo("com.xiaomi.finddevice", "Find Device", OEM.XIAOMI, "Xiaomi"),
        PackageInfo("com.xiaomi.joyose", "Joyose", OEM.XIAOMI, "Xiaomi"),
        PackageInfo("com.xiaomi.market", "GetApps", OEM.XIAOMI, "Xiaomi"),
        PackageInfo("com.xiaomi.midrop", "Mi Drop", OEM.XIAOMI, "Xiaomi"),
        PackageInfo("com.xiaomi.xmsf", "Mi Push Service", OEM.XIAOMI, "Xiaomi"),
        PackageInfo("com.xiaomi.xiaoailite", "XiaoAI", OEM.XIAOMI, "Xiaomi"),
        PackageInfo("com.xiaomi.channel", "Mi Channel", OEM.XIAOMI, "Xiaomi"),
        PackageInfo("com.xiaomi.gamecenter", "Game Center", OEM.XIAOMI, "Xiaomi"),
        PackageInfo("com.xiaomi.mipicks", "GetApps", OEM.XIAOMI, "Xiaomi"),

        // Google Apps (MIUI)
        PackageInfo("com.google.android.googlequicksearchbox", "Google Search", OEM.XIAOMI, "Google"),
        PackageInfo("com.google.android.youtube", "YouTube", OEM.XIAOMI, "Google"),
        PackageInfo("com.google.android.apps.googleassistant", "Google Assistant", OEM.XIAOMI, "Google"),
        PackageInfo("com.google.android.apps.nexuslauncher", "Pixel Launcher", OEM.XIAOMI, "Google"),
        PackageInfo("com.google.android.dialer", "Google Dialer", OEM.XIAOMI, "Google"),
        PackageInfo("com.google.android.apps.messaging", "Messages", OEM.XIAOMI, "Google"),
        PackageInfo("com.google.android.apps.photos", "Photos", OEM.XIAOMI, "Google"),
        PackageInfo("com.google.android.apps.maps", "Maps", OEM.XIAOMI, "Google"),
        PackageInfo("com.google.android.gm", "Gmail", OEM.XIAOMI, "Google"),
        PackageInfo("com.google.android.apps.docs", "Docs", OEM.XIAOMI, "Google"),
        PackageInfo("com.google.android.apps.slides", "Slides", OEM.XIAOMI, "Google"),
        PackageInfo("com.google.android.apps.sheets", "Sheets", OEM.XIAOMI, "Google"),

        // Facebook Apps
        PackageInfo("com.facebook.appmanager", "Facebook App Manager", OEM.XIAOMI, "Facebook"),
        PackageInfo("com.facebook.services", "Facebook Services", OEM.XIAOMI, "Facebook"),
        PackageInfo("com.facebook.system", "Facebook System", OEM.XIAOMI, "Facebook"),
        PackageInfo("com.facebook.katana", "Facebook", OEM.XIAOMI, "Facebook"),
        PackageInfo("com.facebook.orca", "Messenger", OEM.XIAOMI, "Facebook"),
        PackageInfo("com.instagram.android", "Instagram", OEM.XIAOMI, "Facebook"),
    )

    fun getPackages(): List<PackageInfo> = bloatware
}
