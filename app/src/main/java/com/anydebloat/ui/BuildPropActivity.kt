package com.anydebloat.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anydebloat.databinding.ActivityBuildPropBinding
import com.anydebloat.shizuku.ShizukuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BuildPropActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuildPropBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuildPropBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Build Prop Editor"

        loadBuildProp()
        setupButtons()
    }

    private fun loadBuildProp() {
        lifecycleScope.launch {
            binding.progressBar.visibility = android.view.View.VISIBLE
            val content = withContext(Dispatchers.IO) {
                val result = ShizukuService.executeCommand("cat /system/build.prop")
                if (result.first) result.second else "Error: ${result.second}"
            }
            binding.progressBar.visibility = android.view.View.GONE
            binding.tvBuildProp.text = content
        }
    }

    private fun setupButtons() {
        binding.btnPropSearch.setOnClickListener {
            val input = android.widget.EditText(this).apply {
                hint = "Search property (e.g. ro.product.model)"
                inputType = android.text.InputType.TYPE_CLASS_TEXT
            }
            AlertDialog.Builder(this)
                .setTitle("Search Property")
                .setView(input)
                .setPositiveButton("Search") { _, _ ->
                    val query = input.text.toString()
                    if (query.isNotEmpty()) {
                        searchProp(query)
                    }
                }
                .setNegativeButton("Cancel", null).show()
        }

        binding.btnPropAdd.setOnClickListener {
            val layout = android.widget.LinearLayout(this).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                val pad = (16 * resources.displayMetrics.density).toInt()
                setPadding(pad, pad, pad, 0)
            }
            val propInput = android.widget.EditText(this).apply { hint = "Property name" }
            val valInput = android.widget.EditText(this).apply { hint = "Value" }
            layout.addView(propInput)
            layout.addView(valInput)

            AlertDialog.Builder(this)
                .setTitle("Add Property")
                .setView(layout)
                .setPositiveButton("Add") { _, _ ->
                    val prop = propInput.text.toString()
                    val value = valInput.text.toString()
                    if (prop.isNotEmpty() && value.isNotEmpty()) {
                        addProp(prop, value)
                    }
                }
                .setNegativeButton("Cancel", null).show()
        }
    }

    private fun searchProp(query: String) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                ShizukuService.executeCommand("grep '$query' /system/build.prop")
            }
            if (result.first && result.second.isNotEmpty()) {
                binding.tvBuildProp.text = result.second
            } else {
                Toast.makeText(this@BuildPropActivity, "Not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addProp(name: String, value: String) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                ShizukuService.executeCommand("echo '$name=$value' >> /system/build.prop")
            }
            if (result.first) {
                Toast.makeText(this@BuildPropActivity, "Property added (reboot required)", Toast.LENGTH_SHORT).show()
                loadBuildProp()
            } else {
                Toast.makeText(this@BuildPropActivity, "Failed: ${result.second}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
