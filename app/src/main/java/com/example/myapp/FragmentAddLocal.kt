package com.example.myapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentAddLocalBinding
import com.example.myapp.databinding.FragmentAddProductBinding
import com.example.myapp.databinding.FragmentClientMenuBinding
import com.example.myapp.databinding.FragmentFirstBinding
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentAddLocal : Fragment() {

    private var _binding: FragmentAddLocalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddLocalBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGoBack5.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddLocal_to_fragmentAdminMenu)
        }
        binding.buttonAddLocal2.setOnClickListener {
            val db = Firebase.firestore
            val local = hashMapOf(
                "name" to binding.TextName.text.toString(),
                "address" to binding.textadress.text.toString(),
                "time" to binding.editTextTextPersonName12.text.toString()
            )
            db.collection("locals")
                .add(local)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: \${documentReference.id")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            findNavController().navigate(R.id.action_fragmentAddLocal_to_fragmentAdminMenu)
            Toast.makeText(requireContext(), "Lokal dodany", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}