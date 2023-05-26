package com.example.myapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentUpdateBinding
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class FragmentUpdate : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var productsRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = FirebaseDatabase.getInstance()
        productsRef = database.getReference("products")

        binding.buttonGoBack7.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentUpdate_to_fragmentAdminMenu)
        }

        binding.buttonupdatehehe.setOnClickListener {
            val medName = binding.editTextTextPersonName13.text.toString()
            val medPrice = binding.editTextTextPersonName14.text.toString()
            val medDesc = binding.editTextTextPersonName15.text.toString()
            val medIngredients = binding.editTextTextPersonName16.text.toString()
            val medPresc = binding.switch2.isChecked

            val productUpdates = HashMap<String, Any>()
            productUpdates["name"] = medName
            productUpdates["price"] = medPrice
            productUpdates["description"] = medDesc
            productUpdates["ingredients"] = medIngredients
            productUpdates["prescription"] = medPresc

            val medNameRef = productsRef.orderByChild("name").equalTo(medName)

            medNameRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        for (childSnapshot in snapshot.children) {
                            childSnapshot.ref.updateChildren(productUpdates)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Pozycja zaktualizowana", Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.action_fragmentUpdate_to_fragmentAdminMenu)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(requireContext(), "Błąd podczas aktualizacji", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Nie znaleziono produktu o podanej nazwie", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Błąd podczas wyszukiwania produktu", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
