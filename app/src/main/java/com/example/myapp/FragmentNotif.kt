package com.example.myapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentAddLocalBinding
import com.example.myapp.databinding.FragmentAddProductBinding
import com.example.myapp.databinding.FragmentClientMenuBinding
import com.example.myapp.databinding.FragmentFirstBinding
import com.example.myapp.databinding.FragmentNotifBinding
import com.example.myapp.databinding.FragmentUpdateBinding
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

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentNotif : Fragment() {

    private var _binding: FragmentNotifBinding? = null
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val ordersCollection = firestore.collection("orders")

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNotifBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = binding.spinner4

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

        binding.buttonGoBack8.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentNotif_to_fragmentAdminMenu)
        }

        binding.buttonNotif.setOnClickListener {
            val selectedOrder = spinner.selectedItem.toString()
            val email = selectedOrder.substringBefore(" - ")
            val product = selectedOrder.substringAfter(" - ")
            findNavController().navigate(R.id.action_fragmentNotif_to_fragmentAdminMenu)
            ordersCollection.whereEqualTo("name", email)
                .whereEqualTo("products", product)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        document.reference.delete()
                    }
                    Toast.makeText(requireContext(), "Order removed from 'orders' collection", Toast.LENGTH_SHORT).show()

                    val finishedDeliveriesCollection = firestore.collection("finished_deliveries")
                    val newDocument = finishedDeliveriesCollection.document()

                    // Add the email and product fields to the new document
                    newDocument.set(mapOf("name" to email, "products" to product))
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Order added to 'finished_deliveries' collection", Toast.LENGTH_SHORT).show()
                            val client = MailjetClient("d77fd20a54f5449cb935f4b9590f8a29","73bf298e91ff71f5a4ced9f482930552")

                            val request = MailjetRequest(Emailv31.resource)
                                .property(Emailv31.MESSAGES, JSONArray().put(JSONObject().apply {
                                    put(Emailv31.Message.FROM, JSONObject().apply {
                                        put("Email", "calkatym@gmail.com")
                                        put("Name", "Tymu$")
                                    })
                                    put(Emailv31.Message.TO, JSONArray().put(JSONObject().apply {
                                        put("Email", email)
                                        put("Name", "Recipient Name")
                                    }))
                                    put(Emailv31.Message.SUBJECT, "Notification")
                                    put(
                                        Emailv31.Message.HTMLPART, "<h1>Your order has been delivered.</h1>" +
                                                "<p>Go to your application to check</p>")

                                    put(Emailv31.Message.TRACKOPENS, "enabled")


                                }))
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val response: MailjetResponse = client.post(request)
                                    System.out.println(response.getStatus());
                                    System.out.println(response.getData());
                                    // Handle the response

                                } catch (e: MailjetException) {
                                    e.printStackTrace()
                                    // Exception occurred while sending email
                                    // You can add your exception handling code here
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(requireContext(), "Failed to add order to 'finished_deliveries' collection: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Failed to remove order from 'orders' collection: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}