package com.example.myapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.loginButton2.setOnClickListener {
            val username = view.findViewById<EditText>(R.id.editTextTextPersonName)
            val usernametext = username.text.toString()
            val password = view.findViewById<EditText>(R.id.editTextTextPersonName2)
            val passwordtext = password.text.toString()
            if(usernametext.equals("wiktor") && passwordtext.equals("politechnika")){
                Toast.makeText(requireContext(), "Dane prawidłowe", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_SecondFragment_to_fragmentAdminMenu)
            }else if(usernametext.equals("tymek")&& passwordtext.equals("politechnika")){
                Toast.makeText(requireContext(), "Dane prawidłowe", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_SecondFragment_to_clientMenuFragment)
            }
            else{
                Toast.makeText(requireContext(),"źle podane dane", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerButton2.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}