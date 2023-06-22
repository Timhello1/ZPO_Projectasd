package com.example.myapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentNotifBinding
import com.example.myapp.databinding.FragmentScheduleBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.mailjet.client.MailjetClient
import com.mailjet.client.MailjetRequest
import com.mailjet.client.MailjetResponse
import com.mailjet.client.errors.MailjetException
import com.mailjet.client.resource.Emailv31
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class Schedule_Fragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val ordersCollection = firestore.collection("finished_deliveries")

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = binding.spinner5

        // Fetch orders from the Firestore collection
        ordersCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val ordersList = mutableListOf<String>()

                // Iterate through the documents and extract relevant data
                for (document in querySnapshot) {
                    val product = document.getString("products")
                    val email = document.getString("name")
                    val orderText = "$email - $product"
                    ordersList.add(orderText)
                }

                // Create an ArrayAdapter with the orders list and custom item layout
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_item_layout, // Use the custom item layout here
                    ordersList
                )

                // Set the adapter to the Spinner
                spinner.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Toast.makeText(requireContext(), "Failed to fetch orders: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        binding.button9.setOnClickListener {
            findNavController().navigate(R.id.action_schedule_Fragment_to_clientMenuFragment)
        }

        binding.button7.setOnClickListener {
            val selectedOrder = spinner.selectedItem.toString()
            val email = selectedOrder.substringBefore(" - ")
            val product = selectedOrder.substringAfter(" - ")
            findNavController().navigate(R.id.action_schedule_Fragment_to_clientMenuFragment)
            ordersCollection.whereEqualTo("name", email)
                .whereEqualTo("products", product)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        document.reference.delete()
                    }
                    Toast.makeText(
                        requireContext(),
                        "Order removed from 'orders' collection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

}