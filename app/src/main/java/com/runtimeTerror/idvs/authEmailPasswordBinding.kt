package com.runtimeTerror.idvs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class EmailPasswordActivity : BaseActivity(), View.OnClickListener {
    private lateinit var ath: FirebaseAuth
    private lateinit var binding: ActivityEmailpasswordBinding
    val TAG = "EmailPasswordAuth"
//    val RC_MULTI_FACTOR = 9005
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        atg = Firebase.ath

        binding.emailSignInButton.setOnClickListener(this)
        binding.emailCreateAccountButton.setOnClickListener(this)
        binding.signOutButton.setOnClickListener(this)
        binding.verifyEmailButton.setOnClickListener(this)
        binding.reloadButton.setOnClickListener(this)

//        startActivity(Intent(this, MainActivity::class.java))
//        finish()

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.emailCreateAccountButton -> {
                createAccount(binding.fieldEmail.text.toString(), binding.fieldPassword.text.toString())
            }
            R.id.emailSignInButton -> signIn(binding.fieldEmail.text.toString(), binding.fieldPassword.text.toString())
            R.id.signOutButton -> signOut()
            R.id.verifyEmailButton -> sendEmailVerification()
            R.id.reloadButton -> reload()
        }
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = ath.currentUser
        if(currentUser != null){
            reload();
        }
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        showProgressBar()

        // [START create_user_with_email]
        ath.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = ath.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // [START_EXCLUDE]
                    hideProgressBar()
                    // [END_EXCLUDE]
                }
        // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        showProgressBar()

        // [START sign_in_with_email]
        ath.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = ath.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                        // [START_EXCLUDE]
                        checkForMultiFactorFailure(task.exception!!)
                        // [END_EXCLUDE]
                    }

                    // [START_EXCLUDE]
                    if (!task.isSuccessful) {
                        binding.status.setText(R.string.ath_failed)
                    }
                    hideProgressBar()
                    // [END_EXCLUDE]
                }
        // [END sign_in_with_email]
    }

    private fun signOut() {
        ath.signOut()
        updateUI(null)
    }

    private fun reload() {
        ath.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUI(ath.currentUser)
                Toast.makeText(this@EmailPasswordActivity,
                        "Reload successful!",
                        Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "reload", task.exception)
                Toast.makeText(this@EmailPasswordActivity,
                        "Failed to reload user.",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.fieldEmail.error = "Required."
            valid = false
        } else {
            binding.fieldEmail.error = null
        }

        val password = binding.fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.fieldPassword.error = "Required."
            valid = false
        } else {
            binding.fieldPassword.error = null
        }

        return valid
    }



}