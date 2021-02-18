package com.runtimeTerror.idvs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class showStudent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_student)

        val intent = intent
        val studentId = intent.getStringExtra("studentId")
        val studentIdtv = findViewById<TextView>(R.id.studentId)

        studentIdtv.text = studentId

    }
}