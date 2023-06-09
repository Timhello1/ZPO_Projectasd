package com.example.myapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//private var _binding: FragmentRegisterBinding? = null

// This property is only valid between onCreateView and
// onDestroyView.
//private val binding get() = _binding!!

class RegisterActivity : BaseActivity() {
    private var registerButton: Button? = null
    private var inputEmail: EditText? = null
    private var inputName: EditText? = null
    private var inputPassword: EditText? = null
    private var inputRepPass: EditText? = null
    private var goBackButton: Button? = null
    private var adminPass: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)

        adminPass = findViewById(R.id.adminPass)
        registerButton = findViewById(R.id.buttonRegister2)
        inputEmail = findViewById(R.id.inputEmail)
        inputName = findViewById(R.id.inputName)
        inputPassword = findViewById(R.id.inputPassword2)
        inputRepPass = findViewById(R.id.inputPassword2Repeat)
        goBackButton = findViewById(R.id.buttonGoBack)


        registerButton?.setOnClickListener {
            validateRegisterDetails()
            registerUser()

            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        goBackButton?.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }


         private fun validateRegisterDetails(): Boolean {

            return when {
                TextUtils.isEmpty(inputEmail?.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                    false
                }
                TextUtils.isEmpty(inputName?.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar(resources.getString(R.string.err_msg_enter_name), true)
                    false
                }

                TextUtils.isEmpty(inputPassword?.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                    false
                }

                TextUtils.isEmpty(inputRepPass?.text.toString().trim { it <= ' ' }) -> {
                    showErrorSnackBar(resources.getString(R.string.err_msg_enter_reppassword), true)
                    false
                }

                inputPassword?.text.toString().trim { it <= ' ' } != inputRepPass?.text.toString()
                    .trim { it <= ' ' } -> {
                    showErrorSnackBar(resources.getString(R.string.err_msg_password_mismatch), true)
                    false
                }


                else -> {
                    //showErrorSnackBar("Your details are valid",false)
                    true
                }
            }


        }

        fun goToLogin(view: View) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    private fun registerUser() {
        if (validateRegisterDetails()) {
            val email: String = inputEmail?.text.toString().trim() { it <= ' ' }
            val password: String = inputPassword?.text.toString().trim() { it <= ' ' }
            val pass: String = adminPass?.text.toString().trim() { it <= ' ' }
            val login: String = inputName?.text.toString().trim() { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            if(pass.equals("pass")){
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                val db = Firebase.firestore
                                val admin = hashMapOf(
                                    "email" to email,
                                    "password" to password,
                                    "login" to login
                                )
                                db.collection("admins")
                                    .add(admin)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d(TAG, "DocumentSnapshot added with ID: \${documentReference.id")
                                    }
                                    .addOnFailureListener{ e ->
                                        Log.w(TAG, "Error adding document", e)
                                    }
                                showErrorSnackBar(
                                    "You are registered successfully. Your user id is ${firebaseUser.uid}",
                                    false
                                )



                            }else{
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                val db = Firebase.firestore
                                val user = hashMapOf(
                                    "email" to email,
                                    "password" to password,
                                    "login" to login
                                )
                                db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d(TAG, "DocumentSnapshot added with ID: \${documentReference.id")
                                    }
                                    .addOnFailureListener{ e ->
                                        Log.w(TAG, "Error adding document", e)
                                    }
                                showErrorSnackBar(
                                    "You are registered successfully. Your user id is ${firebaseUser.uid}",
                                    false
                                )



                            }



                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }

                    }
                )

        }
    }



    }
