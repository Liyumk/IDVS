package com.runtimeTerror.idvs

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class showStudent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_student)

        val intent = intent
        val TAG = "Student Info"
        val db = FirebaseFirestore.getInstance()
        val scannerStudentId = intent.getStringExtra("studentId")
        val studentIdtv = findViewById<TextView>(R.id.studentId)

        val studentId = scannerStudentId?.replace("/", "-")
//        studentIdtv.text = studentId

        val studentInfo = db.collection("students").document("atr-5388-11")

        studentInfo.get().addOnSuccessListener { document ->
            if(document != null) {
                studentIdtv.text = document.getString("studentFullName")
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
            } else{
                Toast.makeText(this, "The student doesn't exist", Toast.LENGTH_LONG).show()
                Log.e(TAG, "No such document")
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Failed to fetch the document", Toast.LENGTH_LONG).show()
            Log.d(TAG, "get failed with ", exception)
        }



    }

}