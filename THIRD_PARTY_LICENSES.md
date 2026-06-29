# Third-Party Licenses

This project uses the following open-source libraries:

## Shizuku
- **License:** Apache License 2.0
- **URL:** https://github.com/RikkaApps/Shizuku
- **Copyright:** Copyright 2018 Rikka

## Dhizuku
- **License:** Apache License 2.0
- **URL:** https://github.com/iamr0s/Dhizuku
- **Copyright:** Copyright 2021 iamr0s

## Material Components for Android
- **License:** Apache License 2.0
- **URL:** https://github.com/material-components/material-components-android
- **Copyright:** Copyright 2015 Google

## AndroidX Libraries
- **License:** Apache License 2.0
- **URL:** https://github.com/androidx/androidx
- **Copyright:** Copyright 2018 The Android Open Source Project

## Gson
- **License:** Apache License 2.0
- **URL:** https://github.com/google/gson
- **Copyright:** Copyright 2008 Google Inc.

## Kotlin
- **License:** Apache License 2.0
- **URL:** https://github.com/JetBrains/kotlin
- **Copyright:** Copyright 2010-2024 JetBrains s.r.o.

## kotlinx.coroutines
- **License:** Apache License 2.0
- **URL:** https://github.com/Kotlin/kotlinx.coroutines
- **Copyright:** Copyright 2015-2024 JetBrains s.r.o.

## proot / proot-distro
- **License:** GNU General Public License v2.0
- **URL:** https://github.com/proot-me/proot | https://github.com/termux/proot-distro
- **Copyright:** Copyright 2017-2024 PROOT contributors / Termux contributors
- **Note:** proot-distro is used by AnyTool to launch Linux distributions within Termux. AnyTool does not bundle proot-distro; it must be installed separately in Termux via `pkg install proot-distro`.

## Termux
- **License:** GNU General Public License v3.0
- **URL:** https://github.com/termux/termux-app
- **Copyright:** Copyright 2015-2024 Termux contributors
- **Note:** AnyTool optionally integrates with Termux for Linux sandbox terminal mode. Termux must be installed separately by the user.

## Linux Distribution Rootfs
Each distribution referenced (Debian, Ubuntu, Arch Linux, Gentoo, openSUSE, NixOS) maintains its own license:
- **Debian:** GNU General Public License v2.0 — https://www.debian.org/legal/licenses/
- **Ubuntu:** Various open-source licenses — https://ubuntu.com/legal
- **Arch Linux:** GNU General Public License v2.0 — https://archlinux.org/legal/
- **Gentoo:** Various open-source licenses — https://www.gentoo.org/legal/
- **openSUSE:** GNU General Public License v2.0+ — https://en.opensuse.org/Legal
- **NixOS:** GNU General Public License v2.0 — https://nixos.org/nixos/license.html

AnyTool does not bundle any distribution rootfs files. Distributions are downloaded and installed by proot-distro at runtime on the user's device.
