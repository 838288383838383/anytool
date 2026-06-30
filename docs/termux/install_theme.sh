#!/usr/bin/env bash
# Install dependencies and helper scripts in Termux
pkg update -y
pkg install -y curl git lua bash coreutils

# Install cava for audio visualizer (if available)
pkg install -y cava || echo "cava not available in repo"

mkdir -p ~/.anytool/themes
mkdir -p ~/.anytool/scripts

cat > ~/.anytool/scripts/apply_theme.sh <<'EOF'
#!/usr/bin/env bash
THEME_FILE="$1"
if [ -z "$THEME_FILE" ]; then
  echo "Usage: apply_theme.sh <theme_file>"
  exit 1
fi
# Copy theme to app files (requires adb or termux-storage permission)
cp "$THEME_FILE" /data/data/com.anydebloat/files/launcher_themes/ 2>/dev/null || cp "$THEME_FILE" ~/.anytool/themes/
EOF

chmod +x ~/.anytool/scripts/apply_theme.sh

echo "Termux helper scripts installed in ~/.anytool/scripts. Use apply_theme.sh to apply a theme (push to device)."