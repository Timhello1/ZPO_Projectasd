package com.example.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentAddProductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentAddProduct : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var drugName: EditText
    private lateinit var drugPrice: EditText
    private lateinit var drugDescription: EditText
    private lateinit var drugIngredients: EditText
    private lateinit var drugTags: EditText
    private lateinit var drugPrescription: Switch
    //private lateinit var userId: String

    private var _binding: FragmentAddProductBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add_product, container, false)
        drugName = view.findViewById(R.id.drugName)
        drugDescription = view.findViewById(R.id.drugDescription)
        drugIngredients = view.findViewById(R.id.drugIngredients)
        drugTags = view.findViewById(R.id.drugTags)
        drugPrescription = view.findViewById(R.id.isPrescription)
        drugPrice = view.findViewById(R.id.drugPrice)

        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root

    }

    //override
     override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.drugName.isEnabled = true
        binding.drugPrice.isEnabled = true
        binding.drugDescription.isEnabled = true
        binding.drugIngredients.isEnabled = true
        binding.drugTags.isEnabled = true
        binding.isPrescription.isEnabled = true

        val database = Firebase.database.reference
        val productsRef = database.child("products")

        binding.buttonAdd.setOnClickListener {
            val drugName = binding.drugName.text.toString()
            val drugPrice = binding.drugPrice.text.toString()
            val drugDescription = binding.drugDescription.text.toString()
            val drugPrescription = binding.isPrescription.isChecked
            val drugIngredients = binding.drugIngredients.text.toString()
            val drugTags = binding.drugTags.text.toString()
            val imageId = 1

            mAuth = FirebaseAuth.getInstance()
            val userId = mAuth.currentUser?.uid.toString()

            if (userId != null && drugName.isNotEmpty() && drugPrice.isNotEmpty() && drugDescription.isNotEmpty() && drugIngredients.isNotEmpty()) {
                        // Tworzenie obiektu produktu i dodawanie go do bazy danych
                        val productId = productsRef.push().key // Wygenerowanie id dla nowego produktu

                val product = mapOf(
                    "description" to drugDescription,
                    "imageId" to imageId,
                    "sellerId" to userId,
                    "name" to drugName,
                    "prescription" to drugPrescription,
                    "price" to drugPrice,
                    "tags" to drugTags,
                    "ingredients" to drugIngredients
                )
                        if (productId != null) {
                            productsRef.child(productId).setValue(product) // Dodanie produktu do bazy danych
                            Toast.makeText(requireContext(), "Produkt dodany!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_fragmentAddProduct_to_fragmentAdminMenu)

                        } else {
                            Toast.makeText(requireContext(), "Nie udało się dodać produktu.", Toast.LENGTH_SHORT).show()
                        }

            } else {
                Toast.makeText(requireContext(), "Uzupełnij wszystkie pola.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonGoBack4.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddProduct_to_fragmentAdminMenu)
        }

        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    }


