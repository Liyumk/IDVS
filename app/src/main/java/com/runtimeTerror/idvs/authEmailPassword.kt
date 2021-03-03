package com.runtimeTerror.idvs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class authEmailPassword : AppCompatActivity() {
    private lateinit var ath: FirebaseAuth
    val TAG = "EmailPasswordAuth"
//    val RC_MULTI_FACTOR = 9005
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ath = Firebase.ath

        val emailInput = findById<EditText>(R.id.email)
        val passwordInput = findById<EditText>(R.id.password)
        val status = findViewById<EditText>(R.id.validationStatus)
        val sUpBtn = findViewById<Button>(R.id.signUpButton)
        val sInBtn = findViewById<Button>(R.id.signInButton)

        sUpBtn.addOnClickEventListener{
            signUp()
        }
        sInBtn.addOnClickEventListener{
            signIn()
        }

//        email field
//        password
//        sign in btn -> signIn()
//        sign up btn -> signUp()
//        validation status text view

        ath.onAuthStateChanged(user =>{
            if (user != null){
                Toast.makeText(Gravity.BOTTOM, "Logged in.",
                        Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(Gravity.BOTTOM, "Logged out.",
                        Toast.LENGTH_SHORT).show()
            }
        });


    }


    public override fun onStart() {
        super.onStart()
        val currentUser = ath.currentUser
        if(currentUser != null){
            reload();
        }
    }

    private fun signUp(email: String, password: String) {
        if (!validate()) {
            return
        }
        ath.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = ath.currentUser
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(baseContext, "Account creation failed. Try again.",
                                Toast.LENGTH_SHORT).show()
                        emailInput.text = ""
                        passwordInput.text = ""
//                        status.text = "Account creation failed. Try again."
                    }

                }
    }

    private fun signIn(email: String, password: String) {
        if (!validate()) {
            return
        }
        ath.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = ath.currentUser
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(baseContext, "Either email or passwor is incorrect. Try again.",
                                Toast.LENGTH_SHORT).show()
                        emailInput.text = ""
                        passwordInput.text = ""
                    }
                }
    }

//    for signing out
//    private fun signOut() {
//        ath.signOut()
//    }

    private fun reload() {
        ath.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(baseContext,
                        "Reload failed.",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validate(): Boolean {
        var valid = true

        if (emailInput.text == "") {
            status.text = "Email is required."
            valid = false
        }
        if (passwordInput.text == "") {
            status.text = "Password is required."
            valid = false
        }

        return valid
    }



}