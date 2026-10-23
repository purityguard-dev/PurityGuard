package com.purityguard.app

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    companion object {
        private const val VPN_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(50, 100, 50, 100)
        }

        val title = TextView(this).apply {
            text = "PurityGuard"
            textSize = 28f
            setPadding(0, 0, 0, 50)
            gravity = Gravity.CENTER
        }

        val statusText = TextView(this).apply {
            text = "Cloudflare Family DNS (1.1.1.3) Active Filter"
            textSize = 16f
            setPadding(0, 0, 0, 50)
            gravity = Gravity.CENTER
        }

        val toggleButton = Button(this).apply {
            text = "Start PurityGuard VPN"
            setOnClickListener {
                val intent = VpnService.prepare(this@MainActivity)
                if (intent != null) {
                    startActivityForResult(intent, VPN_REQUEST_CODE)
                } else {
                    onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null)
                }
            }
        }

        layout.addView(title)
        layout.addView(statusText)
        layout.addView(toggleButton)
        setContentView(layout)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK) {
            val serviceIntent = Intent(this, PurityVpnService::class.java)
            startService(serviceIntent)
            Toast.makeText(this, "PurityGuard Protected & Filtering Enabled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "VPN Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
}
