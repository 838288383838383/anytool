#!/usr/bin/env bash
# Apply a theme from ~/.anytool/themes to the app files
THEME_NAME="$1"
if [ -z "$THEME_NAME" ]; then
  echo "Usage: apply_theme.sh <theme_name_without_extension>"
  exit 1
fi

THEME_PATH="~/.anytool/themes/${THEME_NAME}.lua"
if [ ! -f "$THEME_PATH" ]; then
  echo "Theme not found: $THEME_PATH"
  exit 2
fi

# Copy to device app files (requires termux-setup-storage and permissions)
TARGET_DIR="/data/data/com.anydebloat/files/launcher_themes"
mkdir -p "$TARGET_DIR"
cp "$THEME_PATH" "$TARGET_DIR/"

echo "Theme $THEME_NAME applied to $TARGET_DIR"