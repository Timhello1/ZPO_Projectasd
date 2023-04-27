package com.example.myapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)

        registerButton = findViewById(R.id.buttonRegister2)
        inputEmail = findViewById(R.id.inputEmail)
        inputName = findViewById(R.id.inputName)
        inputPassword = findViewById(R.id.inputPassword2)
        inputRepPass = findViewById(R.id.inputPassword2Repeat)
        goBackButton = findViewById(R.id.buttonGoBack)

        registerButton?.setOnClickListener {
            //validateRegisterDetails()
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


//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
//        return binding.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.buttonGoBack.setOnClickListener{
//            findNavController().navigate(R.id.action_registerFragment_to_FirstFragment)
//        }
//
//        binding.buttonRegister2.setOnClickListener{
//            findNavController().navigate(R.id.action_registerFragment_to_FirstFragment)
//            Toast.makeText(requireContext(), "Konto zostało stworzone", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }


    private fun registerUser() {
        if (validateRegisterDetails()) {
            val login: String = inputEmail?.text.toString().trim() { it <= ' ' }
            val password: String = inputPassword?.text.toString().trim() { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            showErrorSnackBar(
                                "You are registered successfully. Your user id is ${firebaseUser.uid}",
                                false
                            )


                            FirebaseAuth.getInstance().signOut()
                            finish()


                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }

                    }
                )

        }
    }



    }
