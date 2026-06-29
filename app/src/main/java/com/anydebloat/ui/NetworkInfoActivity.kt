package com.anydebloat.ui

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anydebloat.databinding.ActivityNetworkInfoBinding
import com.anydebloat.shizuku.ShizukuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.NetworkInterface

class NetworkInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNetworkInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetworkInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Network Info"

        loadNetworkInfo()
    }

    private fun loadNetworkInfo() {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val caps = network?.let { cm.getNetworkCapabilities(it) }
        val linkProps = network?.let { cm.getLinkProperties(it) }

        val isWifi = caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        val isMobile = caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true

        binding.tvConnectionType.text = "Type: ${when { isWifi -> "WiFi"; isMobile -> "Mobile"; else -> "None" }}"
        binding.tvNetworkName.text = "Network: ${linkProps?.interfaceName ?: "N/A"}"
        binding.tvDns.text = "DNS: ${linkProps?.dnsServers?.joinToString(", ") ?: "N/A"}"
        val routes = linkProps?.routes?.firstOrNull { it.isDefaultRoute }
        binding.tvGateway.text = "Gateway: ${routes?.gateway?.hostAddress ?: "N/A"}"

        // Get IPs
        val ips = mutableListOf<String>()
        for (intf in NetworkInterface.getNetworkInterfaces()) {
            for (addr in intf.inetAddresses) {
                if (!addr.isLoopbackAddress) {
                    ips.add("${intf.displayName}: ${addr.hostAddress}")
                }
            }
        }
        binding.tvIpAddresses.text = "IP Addresses:\n${ips.joinToString("\n")}"

        // Public IP
        lifecycleScope.launch {
            binding.tvPublicIp.text = "Public IP: Loading..."
            val ip = withContext(Dispatchers.IO) {
                try {
                    java.net.URL("https://api.ipify.org").readText().trim()
                } catch (e: Exception) { "Failed to fetch" }
            }
            binding.tvPublicIp.text = "Public IP: $ip"
        }

        // WiFi details
        if (isWifi) {
            binding.tvWifiDetails.visibility = View.VISIBLE
            val ssid = linkProps?.interfaceName ?: "N/A"
            binding.tvWifiDetails.text = "WiFi SSID: $ssid"
        } else {
            binding.tvWifiDetails.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
