# AnyTool

> **AnyTool** — Android multi-tool powered by Shizuku/Dhizuku. Unbrick, debloat, tweak, and explore your device without root.

An open-source Android utility app that leverages **Shizuku** (ADB without root) and **Dhizuku** (DeviceOwner) to give you deep control over your device.

## Features

### Debloat
- **Normal Mode** — Disable/uninstall bloatware via Shizuku (no root)
- **ZeroDay Mode** — Enhanced removal with deeper system access
- **Brute Mode** — Full root debloat (requires root + Magisk/KernelSU)
- Pre-built package lists for Samsung One UI, Xiaomi MIUI, Generic Android
- Backup before debloat, restore on demand

### System Tools
- **System Tweaks** — Performance and battery optimizations via build.prop
- **Build Prop Editor** — Edit system properties with live preview
- **Permission Manager** — Grant/revoke permissions per app
- **Process Manager** — View and kill running processes
- **App Manager** — Full app info, clear data, force stop, uninstall

### Device Info
- **Battery Stats** — Detailed battery health, temperature, voltage, technology
- **Network Info** — IP, WiFi, mobile data, gateway, DNS, carrier

### Debug
- **Logcat Viewer** — Real-time system logs with filters and level filtering
- **Shell Terminal** — 3 modes: ADB (Shizuku), rish, Linux Sandbox
- **Stress Test** — CPU, memory, disk, and network stress testing
- **Backup/Restore** — APK and data backup management

### Linux Sandbox (NEW)
Run real Linux distributions inside your Android device using **proot-distro** via Termux:
- **Debian** — Stable, reliable, huge package repository
- **Ubuntu** — Most popular Linux desktop distro
- **Arch Linux** — Bleeding-edge rolling release
- **Gentoo** — Compile everything from source
- **openSUSE** — YaST, Btrfs snapshots
- **NixOS** — Declarative, reproducible system config

Requires [Termux](https://github.com/termux/termux-app) installed with `pkg install proot-distro`.

### Extras
- **WiFi Hotspot** — One-switch WiFi sharing (no root needed)
- **Spotify Offline Player** — Play offline tracks from SD card
- **Volume Manager** — Accessibility service shortcuts (volume up 2x = open app)
- **App Launcher** — Themed app grid with search
- **Theme Picker** — 9 themes: Frutiger Aero, Catppuccin, Nord, Dracula, Solarized, Gruvbox, Material You

## Screenshots

> Screenshots coming soon — build and install to try it out!

## Requirements

- Android 8.0+ (API 26)
- [Shizuku](https://github.com/RikkaApps/Shizuku) installed and running (for ADB-based features)
- [Termux](https://github.com/termux/termux-app) (optional, for Linux Sandbox mode)
- Root access (optional, for Brute Mode debloat and root-only features)

## Building

### Prerequisites
- Android Studio Hedgehog+ or command-line Android SDK
- JDK 17+
- Android SDK 34

### Build
```bash
git clone https://github.com/838288383838383/anytool.git
cd anytool
./gradlew assembleDebug
```

The debug APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

## Installation

1. Install [Shizuku](https://github.com/RikkaApps/Shizuku) from Google Play or F-Droid
2. Enable Shizuku (start via wireless debugging or root)
3. Install AnyTool APK
4. Grant AnyTool Shizuku permission when prompted
5. (Optional) Install Termux and `pkg install proot-distro` for Linux Sandbox

## How Shizuku Works

Shizuku runs an ADB service on your device, allowing apps to execute ADB-level commands without a computer. AnyTool uses Shizuku to:
- Disable/uninstall system apps
- Execute shell commands
- Manage permissions
- Access system information

No root required for most features. Root is only needed for Brute Mode debloat.

## How Linux Sandbox Works

The Linux Sandbox mode uses **proot** (a user-space implementation of `chroot`) to run Linux distributions without root access:

1. Termux provides a Linux environment on Android
2. `proot-distro` downloads and manages distribution rootfs images
3. AnyTool sends commands to Termux via the `RunCommandService` API
4. Commands execute inside the chosen distribution's filesystem

This is fully functional Linux — you can install packages, compile software, run servers, and more.

## License

```
Copyright 2024 AnyTool Contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

See [THIRD_PARTY_LICENSES.md](THIRD_PARTY_LICENSES.md) for licenses of third-party dependencies.

## Contributing

Contributions are welcome! Please open an issue first to discuss what you'd like to change.

1. Fork the repo
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Disclaimer

This tool modifies system settings and can disable/remove system apps. **Use at your own risk.** Always create backups before making changes. The authors are not responsible for bricked devices, lost data, or any other damage.

## Acknowledgments

- [Shizuku](https://github.com/RikkaApps/Shizuku) — ADB without root
- [Dhizuku](https://github.com/iamr0s/Dhizuku) — DeviceOwner API
- [Termux](https://github.com/termux/termux-app) — Linux terminal for Android
- [proot-distro](https://github.com/termux/proot-distro) — Linux distribution manager
- [Material Design](https://material.io/) — Design system
