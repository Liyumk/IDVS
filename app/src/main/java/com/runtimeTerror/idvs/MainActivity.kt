package com.runtimeTerror.idvs

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val RECORD_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpCameraPermission()

        val scanBtn = findViewById<Button>(R.id.scanId)
        val mealScan = findViewById<Button>(R.id.mealScan)

        scanBtn.setOnClickListener {
            val intent = Intent(this, scannerActivity::class.java)
            startActivity(intent)
        }

        mealScan.setOnClickListener {
            val intent = Intent(this, mealCardTicker::class.java)
            startActivity(intent)
        }
    }

    private fun setUpCameraPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
            Toast.makeText(this, "Please grant camera permission", Toast.LENGTH_LONG).show()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA),
            RECORD_REQUEST_CODE)
    }
}