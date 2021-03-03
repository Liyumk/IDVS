package com.runtimeTerror.idvs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class authEmailPassword : AppCompatActivity() {
    private lateinit var ath: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val TAG = "EmailPasswordAuth"
    val TAGG = "GoogleActivity"
    val RC_SIGN_IN = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ath = Firebase.ath

        val emailInput = findById<EditText>(R.id.email)
        val passwordInput = findById<EditText>(R.id.password)
        val status = findViewById<EditText>(R.id.validationStatus)
        val sUpBtn = findViewById<Button>(R.id.signUpButton)
        val sInBtn = findViewById<Button>(R.id.signInButton)

        sUpBtn.setOnClickListener{
            signUp()
        }
        sInBtn.setOnClickListener{
            signIn()
        }

        findViewById(R.id.sign_in_button).setOnClickListener(signInG);

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

        val gso: GoogleSignInOptions = Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    public override fun onStart() {
        super.onStart()
        val currentUser = ath.currentUser
        if(currentUser != null){
            reload();
        }
        val account: GoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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
                        Toast.makeText(this, "Account creation failed. Try again.",
                                    //baseContext
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
                        Toast.makeText(this, "Either email or passwor is incorrect. Try again.",
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



//    -----------------------------------
    private open fun signInG(): Unit {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Authenication failed. Try again.",
                        //baseContext
                        Toast.LENGTH_SHORT).show()
                emailInput.text = ""
                passwordInput.text = ""
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Sign in failed. Try again.",
                                //baseContext
                                Toast.LENGTH_SHORT).show()
                        emailInput.text = ""
                        passwordInput.text = ""
                    }
                }
    }


//    private fun signOutG() {
//        googleSignInClient.signOut().addOnCompleteListener(this) {
//            updateUI(null)
//        }
//    }

//    private fun revokeAccess() {
//        // Firebase sign out
//        auth.signOut()
//
//        // Google revoke access
//        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
//            updateUI(null)
//        }
//    }



}