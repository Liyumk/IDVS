package com.runtimeTerror.idvs

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

//import java.time.LocalDateTime
//import java.time.LocalDateTime.now
//import java.time.format.DateTimeFormatter

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

        //get student information
        val scannerStudentId = "atr-5388-11"

        //check if the student exist

        val studentFromFireStore = db.collection("students").document(scannerStudentId)

        studentFromFireStore.get().addOnSuccessListener { student ->
            if(student == null) {
                Toast.makeText(this, "Student doesn't exist. get out!!!", Toast.LENGTH_LONG).show()
            }else{
                val currentCalender = Calendar.getInstance()

                val currentYear = currentCalender.get(Calendar.YEAR)
                val currentMonth = currentCalender.get(Calendar.MONTH)
                val currentDay = currentCalender.get(Calendar.DAY_OF_MONTH)
                val currentHour = currentCalender.get(Calendar.HOUR_OF_DAY)
                val currentMinute = currentCalender.get(Calendar.MINUTE)

                val defaultCalendar = Calendar.getInstance()

                defaultCalendar.set(currentYear, currentMonth, currentDay)

                val currentDate = "$currentDay-$currentMonth-$currentYear"
                val currentTime = "$currentHour:$currentMinute"

                //fetch student meal information
                val studentMealInfo = db.collection("Meals").document(scannerStudentId).collection(currentDate)

                studentMealInfo.get().addOnSuccessListener { meals ->

                    Log.d("Mike", meals.isEmpty().toString())
                    val trueValue = hashMapOf(
                        "value" to true
                    )
                    val falseValue = hashMapOf(
                            "value" to false
                    )
                    if(meals.isEmpty()){
                        studentMealInfo.document("breakfast").set(falseValue)
                        studentMealInfo.document("lunch").set(falseValue)
                        studentMealInfo.document("dinner").set(falseValue)
                    }

                    val checkBreakFast = checkTimeRange("16:00", "17:00", currentTime)
                    val checkLunch = checkTimeRange("11:30", "13:00", currentTime)
                    val checkDinner = checkTimeRange("1:30", "4:00", currentTime)

                    when {
                        checkBreakFast -> {
                            studentMealInfo.document("breakfast").set(trueValue)
                            Toast.makeText(this, "Break Fast", Toast.LENGTH_LONG).show()
                        }
                        checkLunch -> {
                            studentMealInfo.document("lunch").set(trueValue)
                            Toast.makeText(this, "Lunch", Toast.LENGTH_LONG).show()
                        }
                        checkDinner -> {
                            studentMealInfo.document("dinner").set(trueValue)
                            Toast.makeText(this, "Dinner", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this, "Can't get a meal at this time",  Toast.LENGTH_LONG).show()
                        }
                    }



//                    if (mealMap != null) {
//                        if(mealMap.isEmpty()) {
//                            val foods = hashMapOf(
//                                    "breakfast" to false,
//                                    "lunch" to false,
//                                    "dinner" to false,
//                            )
//                            val studentInfo = hashMapOf(
//                                    currentDate to foods
//                            )
//
//                            //add date if it doesn't exist
//                            db.collection("Meals").document(scannerStudentId).set(studentInfo)
//                        }
//                    }
//

//

//                    val studentInfo = hashMapOf(
//                            currentDate to mealMap
//                    )
//
//                    Log.d("Mike: map", mealMap.toString())
//                    Log.d("Mike: Stored Hash", mealMap.toString())
//
//                    if (mealMap != null) {
//                        db.collection("Meals").document(scannerStudentId).delete().addOnSuccessListener {
//                            db.collection("Meals").document(scannerStudentId).set(studentInfo)
//                        }.addOnFailureListener{ exception ->
//                            Toast.makeText(this, "Error: Can't Delete",  Toast.LENGTH_LONG).show()
//                        }
//                    }
//
//                    dateText.text = "$currentHour:$currentMinute"
//
//                    Toast.makeText(this, "Student exist. get in!!!", Toast.LENGTH_LONG).show()
                }.addOnFailureListener{ exception ->
                    Toast.makeText(this, "you can't get a meal at this time",  Toast.LENGTH_LONG).show()
                }

                //student meal fetch error here


            }
        }

        //student fetch error here
    }

    fun checkTimeRange(startTime: String, endTime: String, currentTime: String): Boolean {
        val now = Calendar.getInstance();

        val hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
        val minute = now.get(Calendar.MINUTE);

        val date = parseDate("$hour:$minute");
        val dateCompareOne = parseDate(startTime);
        val dateCompareTwo = parseDate(endTime);

        return dateCompareOne.before( date ) && dateCompareTwo.after(date)
    }

    fun parseDate(date: String): Date {
        val inputFormat = "HH:mm";
        val inputParser = SimpleDateFormat(inputFormat);
        return inputParser.parse(date);

    }
}