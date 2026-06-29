package com.anydebloat.packagelists

import com.anydebloat.models.OEM
import com.anydebloat.models.PackageInfo

object SamsungOneUIPackages {

    private val bloatware = listOf(
        // Samsung Apps
        PackageInfo("com.samsung.android.app.tips", "Tips", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.bixby.agent", "Bixby Agent", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.bixby.service", "Bixby Service", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.visionintelligence", "Bixby Vision", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.orservice", "AR Zone", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.aremoji", "AR Emoji", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.aremojieditor", "AR Emoji Editor", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.app.spage", "Samsung Free", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.spay", "Samsung Pay", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.samsungpass", "Samsung Pass", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.app.sfinder", "S Finder", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.scloud", "Samsung Cloud", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.mobileservice", "Samsung Mobile Service", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.game.gamehome", "Game Launcher", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.game.gametools", "Game Tools", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.app.tips", "SmartTips", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.sec.android.app.sbrowser", "Samsung Browser", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.calendar", "Samsung Calendar", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.app.sharelive", "Quick Share", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.kidsinstaller", "Kids Home", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.app.routines", "Routines", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.forest", "Digital Wellbeing", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.voccleanupmanager", "Voice Cleanup", OEM.SAMSUNG, "Samsung"),

        // Samsung Bloatware
        PackageInfo("com.samsung.android.da.daagent", "Dual Messenger", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.dqpm", "DQPM", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.app.taskedge", "Task Edge", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.app.captureedge", "Edge Panels", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.forest", "Digital Wellbeing", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.mateagent", "SmartThings", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.sec.android.app.sbrowser", "Samsung Internet", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.spay", "Samsung Pay Mini", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.themestore", "Theme Store", OEM.SAMSUNG, "Samsung"),
        PackageInfo("com.samsung.android.app.routines", "Bixby Routines", OEM.SAMSUNG, "Samsung"),

        // Microsoft Apps (Samsung)
        PackageInfo("com.microsoft.skydrive", "OneDrive", OEM.SAMSUNG, "Microsoft"),
        PackageInfo("com.microsoft.office.outlook", "Outlook", OEM.SAMSUNG, "Microsoft"),
        PackageInfo("com.microsoft.office.word", "Word", OEM.SAMSUNG, "Microsoft"),
        PackageInfo("com.microsoft.office.excel", "Excel", OEM.SAMSUNG, "Microsoft"),
        PackageInfo("com.microsoft.office.powerpoint", "PowerPoint", OEM.SAMSUNG, "Microsoft"),
        PackageInfo("com.microsoft.teams", "Teams", OEM.SAMSUNG, "Microsoft"),
        PackageInfo("com.skype.raider", "Skype", OEM.SAMSUNG, "Microsoft"),
        PackageInfo("com.linkedin.android", "LinkedIn", OEM.SAMSUNG, "Microsoft"),
        PackageInfo("com.facebook.appmanager", "Facebook App Manager", OEM.SAMSUNG, "Facebook"),
        PackageInfo("com.facebook.services", "Facebook Services", OEM.SAMSUNG, "Facebook"),
        PackageInfo("com.facebook.system", "Facebook System", OEM.SAMSUNG, "Facebook"),

        // Google Apps (optional, Samsung duplicates)
        PackageInfo("com.google.android.googlequicksearchbox", "Google Search", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.youtube", "YouTube", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.music", "Google Play Music", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.videos", "Google Play Movies", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.apps.googleassistant", "Google Assistant", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.apps.nexuslauncher", "Pixel Launcher", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.dialer", "Google Dialer", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.apps.messaging", "Messages", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.apps.photos", "Photos", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.apps.maps", "Maps", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.gm", "Gmail", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.apps.docs", "Docs", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.apps.slides", "Slides", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.apps.sheets", "Sheets", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.apps.cloudprint", "Cloud Print", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.street", "Street View", OEM.SAMSUNG, "Google"),
        PackageInfo("com.google.android.apps.walletnfcrel", "Google Pay", OEM.SAMSUNG, "Google"),
    )

    fun getPackages(): List<PackageInfo> = bloatware
}
