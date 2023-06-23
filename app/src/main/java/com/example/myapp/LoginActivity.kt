package com.example.myapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LoginActivity : BaseActivity(), View.OnClickListener {

    private var inputLogin: EditText? = null
    private var inputPassword: EditText? = null
    private var loginButton2: Button? = null
    private var goBackButton: Button? = null
    private var resetPasswordTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        inputLogin = findViewById(R.id.inputLogin)
        inputPassword = findViewById(R.id.inputPassword2)
        loginButton2 = findViewById(R.id.loginButton2)
        goBackButton = findViewById(R.id.button_second)

        loginButton2?.setOnClickListener{

            logInRegisteredUser()

        }

        resetPasswordTextView = findViewById(R.id.textView)
        resetPasswordTextView?.setOnClickListener {
            val email = inputLogin?.text.toString().trim()
            if (TextUtils.isEmpty(email)) {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_login), true)
            } else {
                resetPassword(email)
            }
        }

        goBackButton?.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }


    override fun onClick(view: View?) {

    }




    @SuppressLint("StringFormatInvalid")
    private fun validateLoginDetails(): Boolean {

        return when{
            TextUtils.isEmpty(inputLogin?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_login), true)
                false
            }

            TextUtils.isEmpty(inputPassword?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }

            else -> {
                showErrorSnackBar("Your details are valid",false)
                true
            }
        }


    }

    private fun logInRegisteredUser(){


        if(validateLoginDetails()){
            val email = inputLogin?.text.toString().trim(){ it<= ' '}
            val password = inputPassword?.text.toString().trim(){ it<= ' '}

            //Log-in using FirebaseAuth

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->

                    if(task.isSuccessful){
                        val db = Firebase.firestore
                        val adminsRef = db.collection("admins")
                        val query: Query = adminsRef.whereEqualTo("email", email)
                        query.get().addOnCompleteListener{task ->
                            if(task.isSuccessful){
                                for(documentSnapshot in task.result!!){
                                    val admin = documentSnapshot.getString("email")

                                    if(admin != null && admin.equals(email)){
                                        Log.d(TAG, "User Exists")
                                        Toast.makeText(this, "Username exists", Toast.LENGTH_SHORT).show()
                                        showErrorSnackBar("You are logged in successfully.", false)
                                        goToMainActivity(1)

                                    }
                                }
                            }
                        }
                        val usersRef = db.collection("users")
                        val query2: Query = usersRef.whereEqualTo("email",email)
                        query2.get().addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                for(documentSnapshot in task.result!!){
                                    val user = documentSnapshot.getString("email")

                                    if(user != null && user.equals(email)){
                                        Log.d(TAG, "User Exists")
                                        Toast.makeText(this, "Username exists", Toast.LENGTH_SHORT).show()
                                        showErrorSnackBar("You are logged in successfully.", false)
                                        goToMainActivity(2)

                                    }
                                }
                            }
                        }
                    } else{
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }
        }
    }

    open fun goToMainActivity(isLoginSuccessful: Int) {

        val user = FirebaseAuth.getInstance().currentUser;
        val uid = user?.email.toString()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("uID",uid)
        intent.putExtra("isLoginSuccessful", isLoginSuccessful)
        startActivity(intent)

    }

    private fun resetPassword(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showErrorSnackBar("Password reset email sent. Check your inbox.", false)
                } else {
                    showErrorSnackBar("Failed to send password reset email. ${task.exception?.message}", true)
                }
            }
    }


}