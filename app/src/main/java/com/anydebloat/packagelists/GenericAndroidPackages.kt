package com.anydebloat.packagelists

import com.anydebloat.models.OEM
import com.anydebloat.models.PackageInfo

object GenericAndroidPackages {

    private val bloatware = listOf(
        // Google Apps (Generic)
        PackageInfo("com.google.android.googlequicksearchbox", "Google Search", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.youtube", "YouTube", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.music", "Google Play Music", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.videos", "Google Play Movies", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.googleassistant", "Google Assistant", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.nexuslauncher", "Pixel Launcher", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.dialer", "Google Dialer", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.messaging", "Messages", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.photos", "Photos", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.maps", "Maps", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.gm", "Gmail", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.docs", "Docs", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.slides", "Slides", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.sheets", "Sheets", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.cloudprint", "Cloud Print", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.street", "Street View", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.walletnfcrel", "Google Pay", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.chromecast.app", "Google Home", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.googlequicksearchbox", "Google Search", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.wellbeing", "Digital Wellbeing", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.accessibilitysuite", "Android Accessibility Suite", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.marvin.talkback", "Android Accessibility Suite", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.turbo", "Device Health Services", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.safetyhub", "Personal Safety", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.fitness", "Google Fit", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.recorder", "Recorder", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.pixelmigrate", "Pixel Setup", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.apps.setupwizard2", "Android Setup", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.configupdater", "Config Updater", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.onetimeinitializer", "One Time Init", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.partnersetup", "Partner Setup", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.gms.location.history", "Location History", OEM.GENERIC, "Google"),

        // Google Connectivity
        PackageInfo("com.google.android.apps.nbu.paisa.user", "Google Pay", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.gm.exchange", "Exchange Services", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.printservice.recommendation", "Print Service Recommendation", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.syncadapters.calendar", "Calendar Sync", OEM.GENERIC, "Google"),
        PackageInfo("com.google.android.syncadapters.contacts", "Contacts Sync", OEM.GENERIC, "Google"),

        // Facebook Apps
        PackageInfo("com.facebook.appmanager", "Facebook App Manager", OEM.GENERIC, "Facebook"),
        PackageInfo("com.facebook.services", "Facebook Services", OEM.GENERIC, "Facebook"),
        PackageInfo("com.facebook.system", "Facebook System", OEM.GENERIC, "Facebook"),
        PackageInfo("com.facebook.katana", "Facebook", OEM.GENERIC, "Facebook"),
        PackageInfo("com.facebook.orca", "Messenger", OEM.GENERIC, "Facebook"),
        PackageInfo("com.instagram.android", "Instagram", OEM.GENERIC, "Facebook"),
        PackageInfo("com.whatsapp", "WhatsApp", OEM.GENERIC, "Facebook"),

        // Generic System
        PackageInfo("com.android.chrome", "Chrome", OEM.GENERIC, "Browser"),
        PackageInfo("com.android.browser", "AOSP Browser", OEM.GENERIC, "Browser"),
        PackageInfo("com.android.email", "Email", OEM.GENERIC, "Email"),
        PackageInfo("com.android.calendar", "Calendar", OEM.GENERIC, "Calendar"),
        PackageInfo("com.android.deskclock", "Desk Clock", OEM.GENERIC, "Clock"),
        PackageInfo("com.android.calculator2", "Calculator", OEM.GENERIC, "Tools"),
        PackageInfo("com.android.contacts", "Contacts", OEM.GENERIC, "Contacts"),
        PackageInfo("com.android.dialer", "Dialer", OEM.GENERIC, "Phone"),
        PackageInfo("com.android.messaging", "Messaging", OEM.GENERIC, "Messaging"),
        PackageInfo("com.android.camera2", "Camera", OEM.GENERIC, "Camera"),
        PackageInfo("com.android.gallery3d", "Gallery", OEM.GENERIC, "Gallery"),

        // Android System Bloat
        PackageInfo("com.android.vending", "Play Store", OEM.GENERIC, "System"),
        PackageInfo("com.android.providers.downloads", "Download Manager", OEM.GENERIC, "System"),
        PackageInfo("com.android.defcontainer", "Package Access Helper", OEM.GENERIC, "System"),
        PackageInfo("com.android.packageinstaller", "Package Installer", OEM.GENERIC, "System"),
        PackageInfo("com.android.statementservice", "Statement Service", OEM.GENERIC, "System"),
        PackageInfo("com.android.wallpapercropper", "Wallpaper Cropper", OEM.GENERIC, "System"),
        PackageInfo("com.android.webview", "Android System WebView", OEM.GENERIC, "System"),
        PackageInfo("com.android.wallpaperbackup", "Wallpaper Backup", OEM.GENERIC, "System"),
    )

    fun getPackages(): List<PackageInfo> = bloatware
}
