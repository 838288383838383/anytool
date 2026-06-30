#!/usr/bin/env bash
# Neko style theme installer
mkdir -p ~/.anytool/themes
cat > ~/.anytool/themes/neko.lua <<'LUA'
-- Neko theme for AnyTool Launcher
theme_name = "Neko"
primary = "#222831"
accent = "#FFB86B"
background = "/sdcard/Pictures/wallpapers/neko.png"
font = "NotoSansJP"
icon_pack = "neko_pack"
LUA

echo "Neko theme saved to ~/.anytool/themes/neko.lua"