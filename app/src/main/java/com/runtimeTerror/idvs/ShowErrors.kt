package com.runtimeTerror.idvs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class ShowErrors : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_errors)


        val intent = intent

        val message = intent.getStringExtra("message")

        val messageView = findViewById<TextView>(R.id.welcomeTxt)
        val goBackView = findViewById<TextView>(R.id.goBack)

        messageView.text = message

        goBackView.setOnClickListener{
            finish()
        }
    }
}