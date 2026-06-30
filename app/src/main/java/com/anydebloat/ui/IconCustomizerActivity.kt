package com.anydebloat.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.anydebloat.R
import com.anydebloat.icon.*
import com.google.gson.GsonBuilder

class IconCustomizerActivity : AppCompatActivity() {

    private lateinit var ivPreview: ImageView
    private lateinit var layerContainer: LinearLayout
    private var design = IconDesign()
    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icon_customizer)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Icon Customizer"

        ivPreview = findViewById(R.id.ivPreview)
        layerContainer = findViewById(R.id.layerContainer)

        loadSaved()
        setupBackground()
        setupLayers()
        setupOverlay()
        setupActions()
        render()
    }

    private fun setupBackground() {
        val typeSpin = findViewById<Spinner>(R.id.spinnerBgType)
        typeSpin.adapter = adapter(arrayOf("Solid", "Gradient"))
        typeSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                design = design.copy(background = design.background.copy(type = if (pos == 0) "solid" else "gradient"))
                render()
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        val shapeSpin = findViewById<Spinner>(R.id.spinnerBgShape)
        shapeSpin.adapter = adapter(arrayOf("Square", "Circle", "Rounded", "Squircle", "Diamond", "Hexagon", "Star", "Triangle", "Heart"))
        shapeSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                val shapes = arrayOf("square", "circle", "rounded_square", "squircle", "diamond", "hexagon", "star", "triangle", "heart")
                design = design.copy(background = design.background.copy(shape = shapes[pos]))
                render()
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        findViewById<View>(R.id.btnBgColor).setOnClickListener { pickColor { c -> design = design.copy(background = design.background.copy(color = c)); render() } }
        findViewById<View>(R.id.btnBgColorEnd).setOnClickListener { pickColor { c -> design = design.copy(background = design.background.copy(colorEnd = c)); render() } }

        val alpha = findViewById<SeekBar>(R.id.sliderAlpha)
        val tvAlpha = findViewById<TextView>(R.id.tvAlphaValue)
        alpha.max = 100; alpha.progress = 100
        alpha.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, from: Boolean) {
                if (from) { tvAlpha.text = "$p% (UNSTABLE)"; design = design.copy(background = design.background.copy(alpha = p / 100f)); render() }
            }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {}
        })

        val corner = findViewById<SeekBar>(R.id.sliderCornerRadius)
        corner.max = 50; corner.progress = 24
        corner.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, from: Boolean) {
                if (from) { design = design.copy(background = design.background.copy(cornerRadius = p.toFloat())); render() }
            }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {}
        })
    }

    private fun setupLayers() {
        findViewById<Button>(R.id.btnAddLayer).setOnClickListener {
            AlertDialog.Builder(this).setTitle("Add Layer").setItems(arrayOf("Shape", "Text")) { _, w ->
                val t = if (w == 0) "shape" else "text"
                val l = LayerConfig(type = t, shape = "circle", color = "#FF386A20", size = 30f, x = 50f, y = 50f, text = if (t == "text") "A" else "")
                design = design.copy(layers = design.layers + l); rebuildLayers(); render()
            }.show()
        }
    }

    private fun rebuildLayers() {
        layerContainer.removeAllViews()
        design.layers.forEachIndexed { i, l ->
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; setPadding(0, 8, 0, 8) }
            row.addView(TextView(this).apply {
                text = "#${i + 1} ${l.type}: ${if (l.type == "shape") l.shape else "\"${l.text}\""}"
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                setTextColor(Color.WHITE); textSize = 14f
            })
            row.addView(Button(this).apply {
                text = "X"; setTextColor(Color.RED); setBackgroundColor(Color.TRANSPARENT)
                setOnClickListener { design = design.copy(layers = design.layers.toMutableList().apply { removeAt(i) }); rebuildLayers(); render() }
            })
            layerContainer.addView(row)
        }
    }

    private fun setupOverlay() {
        val spin = findViewById<Spinner>(R.id.spinnerOverlayType)
        spin.adapter = adapter(arrayOf("None", "Vignette", "Border", "Glow"))
        spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                val types = arrayOf("none", "vignette", "border", "glow")
                design = design.copy(overlay = design.overlay.copy(enabled = pos > 0, type = types[pos]))
                render()
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
    }

    private fun setupActions() {
        findViewById<Button>(R.id.btnExport).setOnClickListener {
            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "application/json"; putExtra(android.content.Intent.EXTRA_TEXT, gson.toJson(design))
            }
            startActivity(android.content.Intent.createChooser(intent, "Export Design"))
        }
        findViewById<Button>(R.id.btnImport).setOnClickListener {
            val input = EditText(this).apply { hint = "Paste JSON..."; minLines = 5 }
            AlertDialog.Builder(this).setTitle("Import Design").setView(input)
                .setPositiveButton("Import") { _, _ ->
                    try { design = IconRenderer.fromJson(input.text.toString()); render(); toast("Imported: ${design.name}") }
                    catch (e: Exception) { toast("Invalid JSON") }
                }.setNegativeButton("Cancel", null).show()
        }
        findViewById<Button>(R.id.btnCommunity).setOnClickListener { showCommunity() }
        findViewById<Button>(R.id.btnApply).setOnClickListener {
            getSharedPreferences("icon_customizer", MODE_PRIVATE).edit().putString("design", gson.toJson(design)).apply()
            toast("Design saved! Restart to apply.")
        }
    }

    private fun showCommunity() {
        val designs = listOf(
            "Neon Cyber" to """{"name":"Neon Cyber","author":"Community","background":{"type":"gradient","color":"#FF0D0221","colorEnd":"#FF1A0533","shape":"squircle"},"layers":[{"type":"text","text":"NC","fontSize":40,"textColor":"#FF00FFCC","x":50,"y":50,"size":80}],"overlay":{"enabled":true,"type":"glow","color":"#4000FFCC"}}""",
            "Minimal White" to """{"name":"Minimal","author":"Community","background":{"type":"solid","color":"#FFFFFFFF","shape":"circle"},"layers":[{"type":"text","text":"M","fontSize":50,"textColor":"#FF1A1C1E","x":50,"y":50,"size":80}]}""",
            "Sunset" to """{"name":"Sunset","author":"Community","background":{"type":"gradient","color":"#FFFF6B6B","colorEnd":"#FFFF8E53","shape":"rounded_square","cornerRadius":30},"layers":[{"type":"shape","shape":"star","color":"#80FFFFFF","size":40,"x":50,"y":40}]}""",
            "Terminal" to """{"name":"Terminal","author":"Community","background":{"type":"solid","color":"#FF0A0A0A","shape":"square"},"layers":[{"type":"text","text":">_","fontSize":35,"textColor":"#FF00FF00","x":50,"y":50,"size":80}]}""",
            "Ocean" to """{"name":"Ocean","author":"Community","background":{"type":"gradient","color":"#FF005C97","colorEnd":"#FF363795","shape":"circle"},"layers":[{"type":"shape","shape":"hexagon","color":"#60FFFFFF","size":50,"x":50,"y":50}]}""",
        )
        AlertDialog.Builder(this).setTitle("Community Designs")
            .setItems(designs.map { it.first }.toTypedArray()) { _, w ->
                design = IconRenderer.fromJson(designs[w].second); render(); toast("Loaded: ${designs[w].first}")
            }.setNegativeButton("Cancel", null).show()
    }

    private fun pickColor(onPick: (String) -> Unit) {
        val colors = arrayOf("#FF0061A4", "#FF00497D", "#FFFFFFFF", "#FF000000", "#FFFF0000", "#FF00FF00", "#FF0000FF", "#FFFFFF00", "#FFFF00FF", "#FF00FFFF", "#FF386A20", "#FFFF6B6B", "#FF6B5778", "#FF006A6A", "#FF7C5800")
        AlertDialog.Builder(this).setTitle("Color")
            .setItems(colors.map { String.format("#%06X", 0xFFFFFF and Color.parseColor(it)) }.toTypedArray()) { _, w -> onPick(colors[w]) }
            .show()
    }

    private fun render() { ivPreview.setImageBitmap(IconRenderer.render(this, design, 512)) }

    private fun loadSaved() {
        val json = getSharedPreferences("icon_customizer", MODE_PRIVATE).getString("design", null)
        if (json != null) try { design = IconRenderer.fromJson(json) } catch (_: Exception) {}
    }

    private fun save() { getSharedPreferences("icon_customizer", MODE_PRIVATE).edit().putString("design", gson.toJson(design)).apply() }

    private fun adapter(items: Array<String>) = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    override fun onPause() { super.onPause(); save() }
    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
