package com.runtimeTerror.idvs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scanBtn = findViewById<Button>(R.id.scanId)

        scanBtn.setOnClickListener {
            val intent = Intent(this, scannerActivity::class.java)
            startActivity(intent)
        }
    }
}