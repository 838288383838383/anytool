package com.anydebloat.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.anydebloat.R
import com.anydebloat.databinding.ActivityAppLauncherBinding
import com.anydebloat.theme.AppTheme

class AppLauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppLauncherBinding
    private var allApps = mutableListOf<Triple<String, String, Drawable?>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applyTheme()
        loadApps()
        setupSearch()
    }

    private fun applyTheme() {
        val theme = AppTheme.getCurrent(this)
        window.decorView.setBackgroundColor(theme.background)
        binding.searchBar.setBackgroundColor(theme.surface)
        binding.searchBar.setTextColor(theme.onSurface)
    }

    private fun loadApps() {
        val pm = packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
            .sortedBy { it.loadLabel(pm).toString() }

        allApps.clear()
        for (appInfo in packages) {
            allApps.add(Triple(
                appInfo.packageName,
                appInfo.loadLabel(pm).toString(),
                appInfo.loadIcon(pm)
            ))
        }

        renderGrid(allApps)
    }

    private fun renderGrid(apps: List<Triple<String, String, Drawable?>>) {
        binding.appGrid.removeAllViews()
        val theme = AppTheme.getCurrent(this)

        for ((pkg, name, icon) in apps) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_launcher_app, binding.appGrid, false)
            val ivIcon = view.findViewById<ImageView>(R.id.appIcon)
            val tvName = view.findViewById<TextView>(R.id.appName)

            ivIcon.setImageDrawable(icon)
            tvName.text = name
            tvName.setTextColor(theme.onSurface)
            view.setOnClickListener {
                val intent = packageManager.getLaunchIntentForPackage(pkg)
                if (intent != null) startActivity(intent)
            }

            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8)
            }
            view.layoutParams = params
            binding.appGrid.addView(view)
        }
    }

    private fun setupSearch() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.lowercase() ?: ""
                val filtered = allApps.filter { it.second.lowercase().contains(query) }
                renderGrid(filtered)
            }
        })
    }
}
