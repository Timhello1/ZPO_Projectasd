package com.example.myapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LoginActivity : BaseActivity(), View.OnClickListener {

    private var inputLogin: EditText? = null
    private var inputPassword: EditText? = null
    private var loginButton2: Button? = null
    private var goBackButton: Button? = null

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
                        showErrorSnackBar("You are logged in successfully.", false)
                        goToMainActivity(true)
                        finish()

                    } else{
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                        goToMainActivity(false)
                    }
                }
        }
    }

    open fun goToMainActivity(isLoginSuccessful: Boolean) {

        val user = FirebaseAuth.getInstance().currentUser;
        val uid = user?.email.toString()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("uID",uid)
        intent.putExtra("isLoginSuccessful", isLoginSuccessful)
        startActivity(intent)
        finish()
    }
//    private var _binding: FragmentLoginBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//            inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//
//        _binding = FragmentLoginBinding.inflate(inflater, container, false)
//        return binding.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
//
//        binding.loginButton2.setOnClickListener {
//            val username = view.findViewById<EditText>(R.id.inputLogin)
//            val usernametext = username.text.toString()
//            val password = view.findViewById<EditText>(R.id.inputPassword2)
//            val passwordtext = password.text.toString()
//            if(usernametext.equals("wiktor") && passwordtext.equals("politechnika")){
//                Toast.makeText(requireContext(), "Dane prawidłowe", Toast.LENGTH_SHORT).show()
//                findNavController().navigate(R.id.action_SecondFragment_to_fragmentAdminMenu)
//            }else if(usernametext.equals("tymek")&& passwordtext.equals("politechnika")){
//                Toast.makeText(requireContext(), "Dane prawidłowe", Toast.LENGTH_SHORT).show()
//                findNavController().navigate(R.id.action_SecondFragment_to_clientMenuFragment)
//            }
//            else{
//                Toast.makeText(requireContext(),"źle podane dane", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        binding.registerButton2.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_registerFragment)
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

}