package com.anydebloat.packagelists

import com.anydebloat.models.OEM
import com.anydebloat.models.PackageInfo

object PackageDatabase {

    fun getAllPackages(): List<PackageInfo> {
        return SamsungOneUIPackages.getPackages() +
               XiaomiMIUIPackages.getPackages() +
               HyperOSPackages.getPackages() +
               OnePlusPackages.getPackages() +
               RealmePackages.getPackages() +
               OppoPackages.getPackages() +
               VivoPackages.getPackages() +
               GenericAndroidPackages.getPackages()
    }

    fun getPackagesByOEM(oem: OEM): List<PackageInfo> {
        return when (oem) {
            OEM.ALL -> getAllPackages()
            OEM.SAMSUNG -> SamsungOneUIPackages.getPackages()
            OEM.XIAOMI -> XiaomiMIUIPackages.getPackages()
            OEM.HYPEROS -> HyperOSPackages.getPackages()
            OEM.ONEPLUS -> OnePlusPackages.getPackages()
            OEM.REALME -> RealmePackages.getPackages()
            OEM.OPPO -> OppoPackages.getPackages()
            OEM.VIVO -> VivoPackages.getPackages()
            OEM.GENERIC -> GenericAndroidPackages.getPackages()
        }
    }

    fun getCategories(oem: OEM): List<String> {
        return getPackagesByOEM(oem)
            .map { it.category }
            .distinct()
            .sorted()
    }
}
