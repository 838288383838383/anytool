package com.anydebloat.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anydebloat.databinding.ActivityPermissionManagerBinding
import com.anydebloat.shizuku.ShizukuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PermissionManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionManagerBinding

    private val dangerousPermissions = mapOf(
        "CAMERA" to "android.permission.CAMERA",
        "RECORD_AUDIO" to "android.permission.RECORD_AUDIO",
        "ACCESS_FINE_LOCATION" to "android.permission.ACCESS_FINE_LOCATION",
        "ACCESS_COARSE_LOCATION" to "android.permission.ACCESS_COARSE_LOCATION",
        "READ_CONTACTS" to "android.permission.READ_CONTACTS",
        "WRITE_CONTACTS" to "android.permission.WRITE_CONTACTS",
        "READ_SMS" to "android.permission.READ_SMS",
        "SEND_SMS" to "android.permission.SEND_SMS",
        "READ_PHONE_STATE" to "android.permission.READ_PHONE_STATE",
        "CALL_PHONE" to "android.permission.CALL_PHONE",
        "READ_CALL_LOG" to "android.permission.READ_CALL_LOG",
        "WRITE_CALL_LOG" to "android.permission.WRITE_CALL_LOG",
        "READ_EXTERNAL_STORAGE" to "android.permission.READ_EXTERNAL_STORAGE",
        "WRITE_EXTERNAL_STORAGE" to "android.permission.WRITE_EXTERNAL_STORAGE",
        "POST_NOTIFICATIONS" to "android.permission.POST_NOTIFICATIONS",
        "READ_MEDIA_IMAGES" to "android.permission.READ_MEDIA_IMAGES",
        "READ_MEDIA_VIDEO" to "android.permission.READ_MEDIA_VIDEO",
        "READ_MEDIA_AUDIO" to "android.permission.READ_MEDIA_AUDIO",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Permission Manager"

        binding.btnGrant.setOnClickListener { promptAction("grant") }
        binding.btnRevoke.setOnClickListener { promptAction("revoke") }
    }

    private fun promptAction(action: String) {
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            val pad = (16 * resources.displayMetrics.density).toInt()
            setPadding(pad, pad, pad, 0)
        }
        val pkgInput = android.widget.EditText(this).apply { hint = "Package name" }
        val permSpinner = android.widget.Spinner(this)
        val permNames = dangerousPermissions.keys.toTypedArray()
        permSpinner.adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, permNames)
        layout.addView(pkgInput)
        layout.addView(permSpinner)

        AlertDialog.Builder(this)
            .setTitle("Permission: $action")
            .setView(layout)
            .setPositiveButton(action.capitalize()) { _, _ ->
                val pkg = pkgInput.text.toString()
                val perm = dangerousPermissions[permNames[permSpinner.selectedItemPosition]]
                if (pkg.isNotEmpty() && perm != null) {
                    lifecycleScope.launch {
                        val result = withContext(Dispatchers.IO) {
                            if (action == "grant") ShizukuService.grantPermission(pkg, perm)
                            else ShizukuService.revokePermission(pkg, perm)
                        }
                        Toast.makeText(this@PermissionManagerActivity,
                            if (result.first) "Permission $action" else "Failed: ${result.second}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null).show()
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
