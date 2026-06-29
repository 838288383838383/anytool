package com.anydebloat.theme

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.anydebloat.R

enum class AppTheme(
    val displayName: String,
    val description: String,
    val primary: Int,
    val onPrimary: Int,
    val primaryContainer: Int,
    val secondary: Int,
    val secondaryContainer: Int,
    val background: Int,
    val surface: Int,
    val onSurface: Int,
    val cardBg: Int,
    val outline: Int,
    val accent: Int
) {
    // Material 3 Default
    MATERIAL(
        "Material You", "Default Android 16 Material 3",
        Color.parseColor("#FF0061A4"), Color.WHITE,
        Color.parseColor("#FFD1E4FF"), Color.parseColor("#FF535F70"),
        Color.parseColor("#FFD7E3F7"), Color.parseColor("#FFFDFCFF"),
        Color.parseColor("#FFFDFCFF"), Color.parseColor("#FF1A1C1E"),
        Color.WHITE, Color.parseColor("#FFC3C6CF"), Color.parseColor("#FF0061A4")
    ),

    // Frutiger Aero
    FRUTIGER_AERO(
        "Frutiger Aero", "Glossy glass and nature inspired",
        Color.parseColor("#FF2E8B57"), Color.WHITE,
        Color.parseColor("#FFB2DFDB"), Color.parseColor("#FF00796B"),
        Color.parseColor("#FFB2DFDB"), Color.parseColor("#FFE0F2F1"),
        Color.WHITE, Color.parseColor("#FF004D40"),
        Color.WHITE, Color.parseColor("#FF80CBC4"), Color.parseColor("#FF00BFA5")
    ),

    // Catppuccin Mocha
    CATPPUCCIN_MOCHA(
        "Catppuccin Mocha", "Warm dark theme",
        Color.parseColor("#FFCBA6F7"), Color.parseColor("#FF1E1E2E"),
        Color.parseColor("#FF45475A"), Color.parseColor("#FFF38BA8"),
        Color.parseColor("#FF45475A"), Color.parseColor("#FF1E1E2E"),
        Color.parseColor("#FF313244"), Color.parseColor("#FFCDD6F4"),
        Color.parseColor("#FF313244"), Color.parseColor("#FF585B70"), Color.parseColor("#FF89B4FA")
    ),

    // Catppuccin Macchiato
    CATPPUCCIN_MACCHIATO(
        "Catppuccin Macchiato", "Smooth dark theme",
        Color.parseColor("#FFCBA6F7"), Color.parseColor("#FF181926"),
        Color.parseColor("#FF494D64"), Color.parseColor("#FFF38BA8"),
        Color.parseColor("#FF494D64"), Color.parseColor("#FF181926"),
        Color.parseColor("#FF24273A"), Color.parseColor("#FFCAD3F5"),
        Color.parseColor("#FF24273A"), Color.parseColor("#FF5B6078"), Color.parseColor("#FF8AADF4")
    ),

    // Catppuccin Latte (Light)
    CATPPUCCIN_LATTE(
        "Catppuccin Latte", "Clean light theme",
        Color.parseColor("#FF8839EF"), Color.WHITE,
        Color.parseColor("#FFDDD0E7"), Color.parseColor("#FFD20F31"),
        Color.parseColor("#FFE6E9EF"), Color.parseColor("#FFEFF1F5"),
        Color.parseColor("#FFCCD0DA"), Color.parseColor("#FF4C4F69"),
        Color.WHITE, Color.parseColor("#FFCCD0DA"), Color.parseColor("#FF1E66F5")
    ),

    // Nord
    NORD(
        "Nord", "Arctic blue theme",
        Color.parseColor("#FF5E81AC"), Color.WHITE,
        Color.parseColor("#FFD8DEE9"), Color.parseColor("#FF88C0D0"),
        Color.parseColor("#FFE5E9F0"), Color.parseColor("#FFECEFF4"),
        Color.parseColor("#FFECEFF4"), Color.parseColor("#FF2E3440"),
        Color.WHITE, Color.parseColor("#FFD8DEE9"), Color.parseColor("#FF81A1C1")
    ),

    // Dracula
    DRACULA(
        "Dracula", "Dark purple theme",
        Color.parseColor("#FFBD93F9"), Color.parseColor("#FF282A36"),
        Color.parseColor("#FF44475A"), Color.parseColor("#FFFF79C6"),
        Color.parseColor("#FF44475A"), Color.parseColor("#FF282A36"),
        Color.parseColor("#FF44475A"), Color.parseColor("#FFF8F8F2"),
        Color.parseColor("#FF44475A"), Color.parseColor("#FF6272A4"), Color.parseColor("#FF50FA7B")
    ),

    // Solarized Light
    SOLARIZED_LIGHT(
        "Solarized Light", "Warm light theme",
        Color.parseColor("#FF268BD2"), Color.WHITE,
        Color.parseColor("#FFEEE8D5"), Color.parseColor("#FF2AA198"),
        Color.parseColor("#FFFDF6E3"), Color.parseColor("#FFFDF6E3"),
        Color.WHITE, Color.parseColor("#FF586E75"),
        Color.WHITE, Color.parseColor("#FFEEE8D5"), Color.parseColor("#FF93A1A1")
    ),

    // Gruvbox Dark
    GRUVBOX(
        "Gruvbox", "Retro warm dark theme",
        Color.parseColor("#FFFE8019"), Color.parseColor("#FF282828"),
        Color.parseColor("#FF3C3836"), Color.parseColor("#FF8EC07C"),
        Color.parseColor("#FF3C3836"), Color.parseColor("#FF282828"),
        Color.parseColor("#FF3C3836"), Color.parseColor("#FFEBDBB2"),
        Color.parseColor("#FF3C3836"), Color.parseColor("#FF504945"), Color.parseColor("#FF83A598")
    );

    companion object {
        private const val PREFS_NAME = "app_theme_prefs"
        private const val KEY_THEME = "selected_theme"

        fun getCurrent(context: Context): AppTheme {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val name = prefs.getString(KEY_THEME, MATERIAL.name)
            return try {
                valueOf(name!!)
            } catch (e: Exception) {
                MATERIAL
            }
        }

        fun setCurrent(context: Context, theme: AppTheme) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putString(KEY_THEME, theme.name).apply()
        }
    }

    fun applyToCard(view: View) {
        view.setBackgroundColor(cardBg)
    }

    fun applyToText(textView: TextView, primary: Boolean = false) {
        textView.setTextColor(if (primary) this.primary else onSurface)
    }
}
