package com.runtimeTerror.idvs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class mealCardTicker : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_card_ticker)

//        val intent = intent
//        val TAG = "Student Info"
        val db = FirebaseFirestore.getInstance()
//        val scannerStudentId = intent.getStringExtra("studentId")
//        val studentIdtv = findViewById<TextView>(R.id.studentId)



        val dateText = findViewById<TextView>(R.id.myDate)

        val current = LocalDateTime.now()
        val formatted =
        val foods = hashMapOf(
            "breakfast" to null,
            "lunch" to null,
            "dinner" to null,
        )
        val studentInfo = hashMapOf(
            formatted to foods
        )


        db.collection("Meals").document("atr-5388-11")
            .set(studentInfo)
            .addOnSuccessListener { Toast.makeText(this,  "Inserted", Toast.LENGTH_LONG).show()}
            .addOnFailureListener { e -> Toast.makeText(this,  "Inserted" + e, Toast.LENGTH_LONG).show() }

        dateText.text = formatted
    }

}