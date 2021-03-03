package com.runtimeTerror.idvs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import javax.annotation.meta.When


class showStudent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_student)

        val intent = intent
        val TAG = "Student Info"

        val db = FirebaseFirestore.getInstance()
        val scannerStudentId = intent.getStringExtra("studentId")

        val fullName = findViewById<TextView>(R.id.studentName)
        val studentIdView = findViewById<TextView>(R.id.studentId)
        val campus = findViewById<TextView>(R.id.campus)
        val genderView = findViewById<TextView>(R.id.gender)
        val birthDateView = findViewById<TextView>(R.id.birthdate)
        val admission = findViewById<TextView>(R.id.education)
        val goBack = findViewById<TextView>(R.id.goBack)

        val studentId = scannerStudentId?.replace("/", "-")
//        studentIdtv.text = studentId

        val studentInfo = db.collection("students").document("atr-5388-11")

        val msgIntent = Intent(this, ShowErrors::class.java)
        studentInfo.get().addOnSuccessListener { document ->
            if(document != null) {
                var student = document.getData()
                if (student != null) {
                    if(student.isEmpty()) {
                        msgIntent.putExtra("message", "The student doesn't exist")
                        startActivity(msgIntent)
                        Toast.makeText(this, "The student doesn't exist", Toast.LENGTH_LONG).show()
                        Log.e(TAG, "No such student")
                    }else{
                        val studentStatus = document.getString("active");
                        var currentStatus = ""
                        if(studentStatus == "active") {
                            currentStatus = "Access Granted"
                        }else if(studentStatus == "withdrawn") {
                            currentStatus = "Can't Pass Student Withdrawn"
                        }else if(studentStatus == "dropout") {
                            currentStatus = "Can't Pass Student is a Dropout"
                        }else if(studentStatus == "graduated") {
                            currentStatus = "Can't Pass Student graduated"
                        }

                        fullName.text = document.getString("studentFullName")
                        studentIdView.text = studentId
                        genderView.text = document.getString("gender")
                        admission.text = document.getString("admission")
                        campus.text = document.getString("campus")
                        birthDateView.text = document.getString("dateOfBirth")

                    }
                }else{

                    msgIntent.putExtra("message", "The student doesn't exist")
                    startActivity(msgIntent)
                    Toast.makeText(this, "The student doesn't exist", Toast.LENGTH_LONG).show()
                    Log.e(TAG, "No such student")
                }
            } else{
                msgIntent.putExtra("message", "The student doesn't exist")
                startActivity(msgIntent)
                Toast.makeText(this, "The student doesn't exist", Toast.LENGTH_LONG).show()
                Log.e(TAG, "No such student")
            }
        }.addOnFailureListener { exception ->

            msgIntent.putExtra("message", "Failed to fetch the document")
            startActivity(msgIntent)
            Toast.makeText(this, "Failed to fetch the document", Toast.LENGTH_LONG).show()
            Log.d(TAG, "get failed with ", exception)
        }

        goBack.setOnClickListener{
            finish()
        }


    }

}