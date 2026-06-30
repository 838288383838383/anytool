package com.anydebloat.icon

import android.content.Context
import android.graphics.*
import android.net.Uri
import com.google.gson.Gson
import kotlin.math.*

object IconRenderer {

    fun render(context: Context, design: IconDesign, size: Int = 512): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        drawBackground(canvas, paint, design.background, size)
        design.layers.sortedBy { it.type != "image" }.forEach { layer ->
            drawLayer(canvas, paint, layer, size, context)
        }
        if (design.overlay.enabled) drawOverlay(canvas, design.overlay, size)
        return bitmap
    }

    private fun drawBackground(canvas: Canvas, paint: Paint, bg: BackgroundConfig, size: Int) {
        val path = shapePath(bg.shape, size.toFloat(), bg.cornerRadius)
        paint.alpha = (bg.alpha * 255).toInt().coerceIn(0, 255)
        when (bg.type) {
            "solid" -> {
                paint.color = parse(bg.color)
                canvas.drawPath(path, paint)
            }
            "gradient" -> {
                val angle = Math.toRadians(bg.gradientAngle.toDouble())
                val len = size * 0.7f
                val cx = size / 2f; val cy = size / 2f
                paint.shader = LinearGradient(
                    cx - cos(angle).toFloat() * len, cy - sin(angle).toFloat() * len,
                    cx + cos(angle).toFloat() * len, cy + sin(angle).toFloat() * len,
                    parse(bg.color), parse(bg.colorEnd), Shader.TileMode.CLAMP
                )
                canvas.drawPath(path, paint)
                paint.shader = null
            }
        }
        paint.alpha = 255
    }

    private fun drawLayer(canvas: Canvas, paint: Paint, layer: LayerConfig, size: Int, ctx: Context) {
        paint.reset(); paint.isAntiAlias = true
        paint.alpha = (layer.alpha * 255).toInt().coerceIn(0, 255)
        val cx = layer.x / 100f * size
        val cy = layer.y / 100f * size
        val s = layer.size / 100f * size

        canvas.save()
        canvas.rotate(layer.rotation, cx, cy)

        when (layer.type) {
            "shape" -> {
                paint.color = parse(layer.color)
                val p = shapePath(layer.shape, s, s * 0.2f)
                val m = Matrix(); m.postTranslate(cx - s / 2, cy - s / 2); p.transform(m)
                canvas.drawPath(p, paint)
                if (layer.strokeWidth > 0) {
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = layer.strokeWidth / 100f * size
                    paint.color = parse(layer.strokeColor)
                    canvas.drawPath(p, paint)
                }
            }
            "text" -> {
                paint.color = parse(layer.textColor)
                paint.textSize = layer.fontSize / 100f * size
                paint.textAlign = Paint.Align.CENTER
                val fm = paint.fontMetrics
                canvas.drawText(layer.text, cx, cy - (fm.ascent + fm.descent) / 2, paint)
            }
            "image" -> {
                layer.imageUri?.let { uri ->
                    try {
                        ctx.contentResolver.openInputStream(Uri.parse(uri))?.use { stream ->
                            BitmapFactory.decodeStream(stream)?.let { bmp ->
                                canvas.drawBitmap(bmp, null, RectF(cx - s / 2, cy - s / 2, cx + s / 2, cy + s / 2), paint)
                            }
                        }
                    } catch (_: Exception) {}
                }
            }
        }
        canvas.restore()
    }

    private fun drawOverlay(canvas: Canvas, ov: OverlayConfig, size: Int) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        when (ov.type) {
            "vignette" -> {
                paint.shader = RadialGradient(
                    size / 2f, size / 2f, size / 2f,
                    intArrayOf(Color.TRANSPARENT, parse(ov.color)),
                    floatArrayOf(0.4f, 1f), Shader.TileMode.CLAMP
                )
                canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), paint)
            }
            "border" -> {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = ov.borderWidth / 100f * size
                paint.color = parse(ov.borderColor)
                canvas.drawPath(shapePath("rounded_square", size.toFloat(), 24f), paint)
            }
            "glow" -> {
                val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                glowPaint.maskFilter = BlurMaskFilter(ov.intensity * size / 4, BlurMaskFilter.Blur.NORMAL)
                glowPaint.color = parse(ov.color)
                canvas.drawCircle(size / 2f, size / 2f, size / 3f, glowPaint)
            }
        }
    }

    private fun shapePath(shape: String, size: Float, cr: Float): Path {
        val path = Path()
        val r = (cr / 100f * size).coerceAtMost(size / 2)
        when (shape) {
            "circle" -> path.addCircle(size / 2, size / 2, size / 2, Path.Direction.CW)
            "square" -> path.addRect(0f, 0f, size, size, Path.Direction.CW)
            "rounded_square" -> path.addRoundRect(0f, 0f, size, size, r, r, Path.Direction.CW)
            "squircle" -> {
                val k = size * 0.5522847f
                path.moveTo(0f, size / 2)
                path.cubicTo(0f, k, k, 0f, size / 2, 0f)
                path.cubicTo(size - k, 0f, size, k, size, size / 2)
                path.cubicTo(size, size - k, size - k, size, size / 2, size)
                path.cubicTo(k, size, 0f, size - k, 0f, size / 2)
                path.close()
            }
            "diamond" -> { path.moveTo(size / 2, 0f); path.lineTo(size, size / 2); path.lineTo(size / 2, size); path.lineTo(0f, size / 2); path.close() }
            "hexagon" -> { val c = size / 2; val rd = size / 2; for (i in 0..5) { val a = Math.toRadians((60.0 * i) - 90); val x = c + rd * cos(a); val y = c + rd * sin(a); if (i == 0) path.moveTo(x.toFloat(), y.toFloat()) else path.lineTo(x.toFloat(), y.toFloat()) }; path.close() }
            "star" -> { val c = size / 2; val oR = size / 2; val iR = size / 5; for (i in 0..9) { val a = Math.toRadians((36.0 * i) - 90); val rd = if (i % 2 == 0) oR else iR; val x = c + rd * cos(a); val y = c + rd * sin(a); if (i == 0) path.moveTo(x.toFloat(), y.toFloat()) else path.lineTo(x.toFloat(), y.toFloat()) }; path.close() }
            "triangle" -> { path.moveTo(size / 2, 0f); path.lineTo(size, size); path.lineTo(0f, size); path.close() }
            "heart" -> {
                val topCurveHeight = size * 0.3f
                val bulgeControl = size * 0.4f
                path.moveTo(size / 2, size * 0.5f)
                path.cubicTo(size / 2, topCurveHeight, size * 0.2f, 0f, size * 0.2f, topCurveHeight)
                path.cubicTo(size * 0.2f, size * 0.15f, size * 0.35f, 0f, size * 0.5f, size * 0.15f)
                path.cubicTo(size * 0.65f, 0f, size * 0.8f, size * 0.15f, size * 0.8f, topCurveHeight)
                path.cubicTo(size * 0.8f, 0f, size * 0.5f, topCurveHeight, size / 2, size * 0.5f)
                path.lineTo(size * 0.15f, size * 0.8f)
                path.cubicTo(0f, size * 0.95f, size / 2, size, size / 2, size)
                path.cubicTo(size / 2, size, size, size * 0.95f, size * 0.85f, size * 0.8f)
                path.close()
            }
            else -> path.addRoundRect(0f, 0f, size, size, r, r, Path.Direction.CW)
        }
        return path
    }

    private fun parse(color: String): Int {
        return try { Color.parseColor(color) } catch (_: Exception) { Color.WHITE }
    }

    fun toJson(design: IconDesign): String = Gson().toJson(design)
    fun fromJson(json: String): IconDesign = try { Gson().fromJson(json, IconDesign::class.java) } catch (_: Exception) { IconDesign() }
}
