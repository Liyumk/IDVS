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

        val outPuts = hashMapOf<String, Any>()

        //get student information
        val scannerStudentId = "atr-5388-11"

        //check if the student exist

        val studentFromFireStore = db.collection("students").document(scannerStudentId)

        studentFromFireStore.get().addOnSuccessListener { student ->
            if(student == null) {
                Toast.makeText(this, "Student doesn't exist or isn't eligible to get a meal.", Toast.LENGTH_LONG).show()
            }else{

//              outPuts.set("fullName", student.getString("studentFullName").toString())
//                outPuts.set("studentId", student.getString("studentId").toString())

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

                outPuts.set("currentDate", currentDate)
                var statusMessage = ""
                //fetch student meal information
                val studentMealInfo = db.collection("Meals").document(scannerStudentId).collection(currentDate)

                studentMealInfo.get().addOnSuccessListener { meals ->

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

                    val checkBreakFast = checkTimeRange("1:00", "4:00", currentTime)
                    val checkLunch = checkTimeRange("11:30", "13:00", currentTime)
                    val checkDinner = checkTimeRange("16:30", "14:00", currentTime)

                    when {
                        checkBreakFast -> {
                            val checkBreakFast = studentMealInfo.document("breakfast")
                            checkBreakFast.get().addOnSuccessListener { bfast ->
                                val bmap = bfast.getData()
                                if(bmap?.get("value") == true) {
                                    statusMessage = "Student already ate breakfast"
                                    Toast.makeText(this, "Already Ate", Toast.LENGTH_LONG).show()
                                }else{
                                    studentMealInfo.document("breakfast").set(trueValue).addOnSuccessListener {
                                       statusMessage = "Student can get breakfast"
                                        Toast.makeText(this, "Break Fast", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                        checkLunch -> {
                            val checkLunch = studentMealInfo.document("lunch")
                            checkLunch.get().addOnSuccessListener { lfast ->
                                val lmap = lfast.getData()
                                if(lmap?.get("value") == true) {
                                    statusMessage = "Student already ate lunch"
                                    Toast.makeText(this, "Already Ate", Toast.LENGTH_LONG).show()
                                }else{
                                    studentMealInfo.document("lunch").set(trueValue).addOnSuccessListener {
                                        statusMessage = "Student can get breakfast"
                                        Toast.makeText(this, "Break Fast", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                        checkDinner -> {
                            val checkDinner = studentMealInfo.document("dinner")
                            checkDinner.get().addOnSuccessListener { dfast ->
                                val dmap = dfast.getData()
                                if(dmap?.get("value") == true) {
                                    statusMessage = "Student already ate Dinner"
                                    Toast.makeText(this, "Already Ate", Toast.LENGTH_LONG).show()
                                }else{
                                    studentMealInfo.document("dinner").set(trueValue).addOnSuccessListener {
                                        statusMessage = "Student can get dinner"
                                        Toast.makeText(this, "Dinner", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                        else -> {
                            statusMessage = "Can't get a meal at this time"
                            Toast.makeText(this, "Can't get a meal at this time",  Toast.LENGTH_LONG).show()
                        }
                    }
                    outPuts.set("status", statusMessage)


                }.addOnFailureListener{ exception ->
                    Toast.makeText(this, "you can't get a meal at this time",  Toast.LENGTH_LONG).show()
                }


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
