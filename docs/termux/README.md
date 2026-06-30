AnyTool Termux Theme Scripts

Usage:

1. Install helper scripts in Termux:

   bash install_theme.sh

2. Create or edit themes:

   bash themes/android16.sh
   bash themes/neko.sh

3. Apply a theme to device (requires permissions):

   bash apply_theme.sh android16

Notes:
- These scripts are helpers for quickly generating Lua theme files.
- To import a raw Lua theme from a URL, use the launcher app settings or the LuaConfig import function.
- Make sure Termux has storage permissions (termux-setup-storage) to copy to /sdcard or app files.
