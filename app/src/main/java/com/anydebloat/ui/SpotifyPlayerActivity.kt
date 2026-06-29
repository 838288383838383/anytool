package com.anydebloat.ui

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anydebloat.databinding.ActivitySpotifyPlayerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64
import org.json.JSONObject

class SpotifyPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpotifyPlayerBinding
    private var mediaPlayer: MediaPlayer? = null
    private var currentTrackIndex = 0
    private var trackList = mutableListOf<File>()
    private var isPlaying = false
    private var accessToken: String? = null

    companion object {
        private const val PREFS = "spotify_prefs"
        private const val KEY_CLIENT_ID = "client_id"
        private const val KEY_CLIENT_SECRET = "client_secret"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpotifyPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Spotify Offline Player"

        loadCredentials()
        setupButtons()
        loadTracks()
    }

    private fun loadCredentials() {
        val prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val clientId = prefs.getString(KEY_CLIENT_ID, "")
        val clientSecret = prefs.getString(KEY_CLIENT_SECRET, "")

        if (clientId.isNullOrEmpty() || clientSecret.isNullOrEmpty()) {
            showCredentialsDialog()
        } else {
            binding.tvStatus.text = "Credentials loaded"
        }
    }

    private fun showCredentialsDialog() {
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            val pad = (16 * resources.displayMetrics.density).toInt()
            setPadding(pad, pad, pad, 0)
        }
        val idInput = android.widget.EditText(this).apply { hint = "Spotify Client ID" }
        val secretInput = android.widget.EditText(this).apply { hint = "Spotify Client Secret" }
        layout.addView(idInput)
        layout.addView(secretInput)

        AlertDialog.Builder(this)
            .setTitle("Spotify API Credentials")
            .setMessage("Enter your Spotify Bot/App credentials")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                prefs.edit()
                    .putString(KEY_CLIENT_ID, idInput.text.toString())
                    .putString(KEY_CLIENT_SECRET, secretInput.text.toString())
                    .apply()
                binding.tvStatus.text = "Credentials saved"
            }
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .show()
    }

    private fun setupButtons() {
        binding.btnPlayPause.setOnClickListener {
            if (isPlaying) pauseTrack() else playTrack()
        }
        binding.btnNext.setOnClickListener { nextTrack() }
        binding.btnPrev.setOnClickListener { prevTrack() }
        binding.btnRefresh.setOnClickListener { loadTracks() }
        binding.btnSettings.setOnClickListener { showCredentialsDialog() }
    }

    private fun loadTracks() {
        trackList.clear()
        val musicDir = File(Environment.getExternalStorageDirectory(), "Music/Spotify")
        if (!musicDir.exists()) musicDir.mkdirs()

        val tracks = musicDir.listFiles { file -> file.extension in listOf("mp3", "ogg", "flac", "wav", "m4a") }
        if (tracks != null) {
            trackList.addAll(tracks.sortedBy { it.name })
        }

        binding.tvTrackCount.text = "${trackList.size} tracks found"
        binding.tvTrackName.text = if (trackList.isNotEmpty()) trackList[0].nameWithoutExtension else "No tracks"
        binding.tvStatus.text = "Source: ${musicDir.absolutePath}"
    }

    private fun playTrack() {
        if (trackList.isEmpty()) {
            Toast.makeText(this, "No tracks found in Music/Spotify/", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(trackList[currentTrackIndex].absolutePath)
                prepare()
                start()
                setOnCompletionListener { nextTrack() }
            }
            isPlaying = true
            binding.btnPlayPause.text = "Pause"
            binding.tvTrackName.text = trackList[currentTrackIndex].nameWithoutExtension
            binding.tvStatus.text = "Playing: ${currentTrackIndex + 1}/${trackList.size}"
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pauseTrack() {
        mediaPlayer?.pause()
        isPlaying = false
        binding.btnPlayPause.text = "Play"
    }

    private fun nextTrack() {
        if (trackList.isEmpty()) return
        currentTrackIndex = (currentTrackIndex + 1) % trackList.size
        if (isPlaying) playTrack()
        else binding.tvTrackName.text = trackList[currentTrackIndex].nameWithoutExtension
    }

    private fun prevTrack() {
        if (trackList.isEmpty()) return
        currentTrackIndex = if (currentTrackIndex - 1 < 0) trackList.size - 1 else currentTrackIndex - 1
        if (isPlaying) playTrack()
        else binding.tvTrackName.text = trackList[currentTrackIndex].nameWithoutExtension
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
