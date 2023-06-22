package com.example.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
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


class TransferShopFragment : Fragment() {

    private lateinit var spinner: Spinner
    private lateinit var button: Button;
    private var currentUser: FirebaseUser? = null
    private var currentUserEmail: String? = null
    private lateinit var ordersRef: DatabaseReference
    private lateinit var productsRef: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_transfer_shop, container, false)

        spinner = view.findViewById(R.id.spinner2)

        button = view.findViewById(R.id.button4)

        // Pobierz dane z Firestore i zaktualizuj rozwijaną listę
        getDataFromFirestore()

        button.setOnClickListener {
            currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                currentUserEmail = currentUser!!.getEmail()

                fetchOrders(currentUserEmail, object : TransferAddressFragment.EmailTaskCallback {


                    override fun onOrdersFetched(orders: String?, sum: Double) {
                        sendEmail(orders!!, sum)
                        deleteUserOrders()
                        findNavController().navigate(R.id.action_transferShopFragment_to_clientMenuFragment)
                    }
                })
            }
        }

        return view
    }

    private fun deleteUserOrders() {
        if (currentUser != null) {
            val userId = currentUser!!.uid
            ordersRef = FirebaseDatabase.getInstance().getReference("baskets")
            ordersRef.orderByChild("userName").equalTo(currentUserEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (orderSnapshot in dataSnapshot.children) {
                            orderSnapshot.ref.removeValue()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error if needed
                    }
                })
        }
    }

    private fun fetchOrders(
        currentUserEmail: String?,
        callback: TransferAddressFragment.EmailTaskCallback
    ) {
        ordersRef = Firebase.database.reference.child("baskets")
        val query: Query = ordersRef.orderByChild("userName").equalTo(currentUserEmail)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val ordersList = mutableListOf<String>()
                    var sum = 0.0

                    for (orderSnapshot in dataSnapshot.children) {
                        val productName = orderSnapshot.child("name").getValue(String::class.java)
                        val priceString = orderSnapshot.child("price").getValue(String::class.java)

                        val price = priceString?.replace("[^\\d.]".toRegex(), "")?.toDoubleOrNull()
                        ordersList.add("$productName: zl$price")
                        sum += price?.toDouble() ?: 0.0
                    }

                    val ordersString = ordersList.joinToString("\n")
                    callback.onOrdersFetched(ordersString, sum)
                } else {
                    callback.onOrdersFetched("", 0.0)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error if needed
                callback.onOrdersFetched("", 0.0)
            }
        })
    }

    private fun sendEmail(orders: String, sum: Double) {
        val user = FirebaseAuth.getInstance().currentUser
        val userEmail = user?.email

        val address = spinner.selectedItem.toString()


        val client = MailjetClient("d77fd20a54f5449cb935f4b9590f8a29","73bf298e91ff71f5a4ced9f482930552")

        val request = MailjetRequest(Emailv31.resource)
            .property(Emailv31.MESSAGES, JSONArray().put(JSONObject().apply {
                put(Emailv31.Message.FROM, JSONObject().apply {
                    put("Email", "calkatym@gmail.com")
                    put("Name", "Tymu$")
                })
                put(Emailv31.Message.TO, JSONArray().put(JSONObject().apply {
                    put("Email", userEmail)
                    put("Name", "Recipient Name")
                }))
                put(Emailv31.Message.SUBJECT, "Receipt")
                put(
                    Emailv31.Message.HTMLPART, "<h1>Proszę opłacić swój rachunek</h1>" +
                        "<p>Przelew na konto 123214236245748548233521</p>" +
                        "<p>Pod tytułem przelej mi</p>" +
                        "<p>Orders:</p>" +
                        "<p>$orders</p>" +
                        "<p>Total: zl$sum</p>" +
                        "<p> Po opłaceniu zamówienie zostanie dostarczone do sklepu: $address </p>")

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

    internal interface EmailTaskCallback {
        fun onOrdersFetched(
            orders: String?,
            sum: Double
        )
    }

    private fun getDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        // Pobierz kolekcję "locals" z Firestore
        db.collection("locals")
            .get()
            .addOnSuccessListener { result ->
                val locations = mutableListOf<String>() // Tutaj przechowujemy nazwy lokalizacji

                // Przetwarzaj wyniki zapytania
                for (document in result) {
                    val name = document.getString("name")
                    name?.let {
                        locations.add(it)
                    }
                }

                // Utwórz adapter dla rozwijanej listy
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, locations)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Obsłuż błąd pobierania danych
            }
    }


}