package com.purityguard.app

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import java.io.IOException

class PurityVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Configure the local VPN interface
        val builder = Builder()
            .setSession("PurityGuard DNS Filter")
            // Local IP address assigned to the VPN interface
            .addAddress("10.0.0.2", 24)
            // Primary Cloudflare Family DNS (Blocks adult content & malware)
            .addDnsServer("1.1.1.3")
            // Secondary Cloudflare Family DNS
            .addDnsServer("1.0.0.3")
            // Route all DNS traffic through this interface
            .addRoute("1.1.1.3", 32)
            .addRoute("1.0.0.3", 32)

        try {
            vpnInterface = builder.establish()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            vpnInterface?.close()
            vpnInterface = null
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
