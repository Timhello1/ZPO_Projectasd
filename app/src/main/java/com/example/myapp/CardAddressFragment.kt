package com.example.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
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
import java.util.*


class CardAddressFragment : Fragment() {

    private lateinit var button: Button;
    private var currentUser: FirebaseUser? = null
    private var currentUserEmail: String? = null
    private lateinit var ordersRef: DatabaseReference
    private lateinit var productsRef: DatabaseReference
    private lateinit var editTextCVV: EditText
    private lateinit var editTextCard: EditText
    private lateinit var editTextExpiryDate: EditText


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_card_address, container, false)

        editTextCard = view.findViewById(R.id.editTextTextPersonName18)
        editTextCVV = view.findViewById(R.id.editTextTextPersonName20)
        editTextExpiryDate = view.findViewById(R.id.editTextTextPersonName22)




        button = view.findViewById(R.id.button6)



        button.setOnClickListener {
            val cardNumber = editTextCard.text.toString()
            val cvv = editTextCVV.text.toString()
            val expiryDate = editTextExpiryDate.text.toString()

            if (validateCardNumber(cardNumber) && validateCVV(cvv) && validateExpiryDate(expiryDate)) {
                // Wszystkie pola karty są poprawne, wykonaj odpowiednie działania
                // np. wysłanie zamówienia
                currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    currentUserEmail = currentUser!!.getEmail()

                    fetchOrders(currentUserEmail, object : TransferAddressFragment.EmailTaskCallback {
                        override fun onOrdersFetched(orders: String?, sum: Double) {
                            sendEmail(orders!!, sum)
                            deleteUserOrders()
                            findNavController().navigate(R.id.action_cardAddressFragment_to_clientMenuFragment)
                        }
                    })
                }
            } else {
                // Co najmniej jedno z pól karty jest niepoprawne, wyświetl komunikat o błędzie
                Toast.makeText(requireContext(), "Niepoprawne dane karty", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun sendEmail(orders: String, sum: Double) {
        val user = FirebaseAuth.getInstance().currentUser
        val userEmail = user?.email

        val cityEditText = getView()?.findViewById<EditText>(R.id.editTextTextPersonName4)
        val streetEditText = getView()?.findViewById<EditText>(R.id.editTextTextPersonName19)
        val houseNumberEditText = getView()?.findViewById<EditText>(R.id.editTextTextPersonName21)

        val city = cityEditText?.text.toString()
        val street = streetEditText?.text.toString()
        val houseNumber = houseNumberEditText?.text.toString()


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
                    Emailv31.Message.HTMLPART, "<h1>Zamówienie opłacone</h1>" +
                        "<p>Orders:</p>" +
                        "<p>$orders</p>" +
                        "<p>Total: zl$sum</p>" +
                        "<p> Po opłaceniu zamówienie zostanie dostarczone na adres: $city $street $houseNumber</p>")

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

    private fun validateCardNumber(cardNumber: String): Boolean {
        val cleanedCardNumber = cardNumber.replace("\\s|-".toRegex(), "")

        // Sprawdź, czy numer karty ma prawidłową długość (np. 16 cyfr dla większości kart kredytowych)
        if (cleanedCardNumber.length != 16) {
            return false
        }

        // Sprawdź, czy numer karty składa się tylko z cyfr
        if (!cleanedCardNumber.matches("\\d+".toRegex())) {
            return false
        }


        return true
    }

    private fun validateCVV(cvv: String): Boolean {
        if (cvv.length != 3) {
            return false
        }

        // Sprawdź, czy numer CVV składa się tylko z cyfr
        if (!cvv.matches("\\d+".toRegex())) {
            return false
        }

        return true
    }

    private fun validateExpiryDate(expiryDate: String): Boolean {
        // Sprawdź, czy data ważności ma prawidłowy format (np. MM/RR)
        val pattern = "\\d{2}/\\d{2}".toRegex()
        if (!expiryDate.matches(pattern)) {
            return false
        }

        // Podziel datę na miesiąc i rok
        val parts = expiryDate.split("/")
        val month = parts[0].toIntOrNull()
        val year = parts[1].toIntOrNull()

        // Sprawdź, czy miesiąc i rok mają wartości numeryczne
        if (month == null || year == null) {
            return false
        }

        // Sprawdź, czy miesiąc mieści się w zakresie od 1 do 12
        if (month < 1 || month > 12) {
            return false
        }

        // Sprawdź, czy rok jest większy niż bieżący rok
        val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
        if (year < currentYear) {
            return false
        }

        return true
    }


}