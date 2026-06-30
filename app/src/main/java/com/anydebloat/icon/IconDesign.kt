package com.anydebloat.icon

data class IconDesign(
    val name: String = "Custom",
    val author: String = "Unknown",
    val version: Int = 1,
    val background: BackgroundConfig = BackgroundConfig(),
    val layers: List<LayerConfig> = emptyList(),
    val overlay: OverlayConfig = OverlayConfig()
)

data class BackgroundConfig(
    val type: String = "solid",
    val color: String = "#FF0061A4",
    val colorEnd: String = "#FF00497D",
    val gradientAngle: Int = 180,
    val imageUri: String? = null,
    val shape: String = "rounded_square",
    val cornerRadius: Float = 24f,
    val alpha: Float = 1.0f
)

data class LayerConfig(
    val type: String = "shape",
    val shape: String = "circle",
    val color: String = "#FFFFFFFF",
    val size: Float = 50f,
    val x: Float = 50f,
    val y: Float = 50f,
    val rotation: Float = 0f,
    val alpha: Float = 1.0f,
    val strokeWidth: Float = 0f,
    val strokeColor: String = "#00000000",
    val text: String = "",
    val fontSize: Float = 24f,
    val textColor: String = "#FFFFFFFF",
    val imageUri: String? = null,
    val iconName: String = "",
    val animated: Boolean = false,
    val animationType: String = "none",
    val animationSpeed: Float = 1.0f
)

data class OverlayConfig(
    val enabled: Boolean = false,
    val type: String = "noise",
    val color: String = "#40000000",
    val intensity: Float = 0.3f,
    val borderColor: String = "#FFFFFFFF",
    val borderWidth: Float = 2f
)
