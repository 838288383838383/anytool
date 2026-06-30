#!/usr/bin/env bash
# Android 16 theme installer template
# This script writes a simple theme file for the launcher
mkdir -p ~/.anytool/themes
cat > ~/.anytool/themes/android16.lua <<'LUA'
-- Android 16 theme for AnyTool Launcher
theme_name = "Android 16"
primary = "#1F2937"
accent = "#0EA5A4"
background = "/sdcard/Pictures/wallpapers/android16.jpg"
font = "Roboto"
icon_pack = "android16_pack"
LUA

echo "Android 16 theme saved to ~/.anytool/themes/android16.lua"