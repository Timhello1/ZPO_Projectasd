package com.example.myapp

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var productNameTextView: TextView
    private lateinit var productDescriptionTextView: TextView
    private lateinit var productPriceTextView: TextView
    private lateinit var productIngredientsTextView: TextView // dodaj deklarację dla tego pola
    private lateinit var productTagsTextView: TextView // dodaj deklarację dla tego pola
    private lateinit var productPrescriptionTextView: TextView // dodaj deklarację dla tego pola

    private lateinit var productId: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sellerId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_details_activity)

        val buttonGoBackFromDetails = findViewById<Button>(R.id.buttonGoBackFromDetails)
        val buttonAddToCart = findViewById<Button>(R.id.buttonAddToCart)


        productNameTextView = findViewById(R.id.product_name)
        productDescriptionTextView = findViewById(R.id.product_description)
        productIngredientsTextView = findViewById(R.id.product_ingredients)
        productPriceTextView = findViewById(R.id.product_price)
        productTagsTextView = findViewById(R.id.product_tags)
        productPrescriptionTextView = findViewById(R.id.product_prescription)


        val product = intent.getParcelableExtra<Product>("product")

        if (product != null) {
            productNameTextView.text = product.name
        }
        if (product != null) {
            productDescriptionTextView.text = product.description
        }
        if (product != null) {
            productIngredientsTextView.text = product.ingredients
        }
        if (product != null) {
            productPriceTextView.text = "${product.price}zł"
        }
        if (product != null) {
            productTagsTextView.text = product.tags
        }
        if (product != null) {
            productPrescriptionTextView.text = if (product.prescription) "Yes" else "No"
        }

        // Get the product ID passed from the previous activity
        productId = intent.getStringExtra("productId") ?: ""

        // Get the Firebase database reference to the products node
        databaseReference =
            FirebaseDatabase.getInstance().reference.child("products").child(productId)

        // Add a value event listener to retrieve the product data
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get the product data and display it in the UI
                val productName = dataSnapshot.child("name").value as String?
                val productDescription = dataSnapshot.child("description").value as String?
                val productPrice = dataSnapshot.child("price").value as String?
                val productIngredients = dataSnapshot.child("ingredients").value as String?
                val productTags = dataSnapshot.child("tags").value as String?
                val productPrescription = dataSnapshot.child("prescription").value as Boolean?
                val productSellerId = dataSnapshot.child("sellerId").value as String?

                productNameTextView.text = productName ?: "Nazwa nieznana"
                productDescriptionTextView.text = productDescription ?: "Opis niedostępny"
                productPriceTextView.text = "${productPrice} zł" ?: "Cena nieznana"
                // dodaj TextView dla każdego pola
                // np. val ingredientsTextView = findViewById(R.id.product_ingredients)
                productIngredientsTextView.text = productIngredients ?: "Skład nieznany"
                productTagsTextView.text = productTags ?: "Tagi nieznane"
                productPrescriptionTextView.text =
                    if (productPrescription == true) "Wymagana recepta" else if (productPrescription == false) "Nie wymagana recepta" else ((productPrescription
                        ?: "Recepta nieznana").toString())

                // analogicznie dla reszty pól


                if (productSellerId != null) {
                    // Wykonaj działania związane z sellerId
                    sellerId = productSellerId
                } else {
                    // Jeśli sellerId jest null, obsłuż ten przypadek
                    Toast.makeText(
                        this@ProductDetailsActivity,
                        "Nie znaleziono sellerId dla tego produktu.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Obsłuż błąd odczytu danych
                Toast.makeText(
                    this@ProductDetailsActivity,
                    "Wystąpił błąd podczas odczytu danych: " + databaseError.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            })



        buttonGoBackFromDetails.setOnClickListener { this.onBackPressed() }

        buttonAddToCart.setOnClickListener {
            val firebaseAuth = FirebaseAuth.getInstance()
            val currentUser = firebaseAuth.currentUser

            // Sprawdź, czy użytkownik jest zalogowany
            if (currentUser != null) {
                val userId = currentUser.uid
                val userName = currentUser.email


                val databaseReference = FirebaseDatabase.getInstance().reference.child("baskets")

                // Tworzenie nowego rekordu w bazie danych
                val newRecord = databaseReference.push()

                // Pobieranie danych produktu
                val productName = productNameTextView.text.toString()
                val productPrice = productPriceTextView.text.toString()
                val productSellerId = sellerId // Pobierz id sprzedawcy danego produktu (sellerId)

                // Tworzenie obiektu produktu
                val product = HashMap<String, Any>()
                product["userId"] = userId
                product["userName"] = userName ?: "Nieznany użytkownik"
                product["productId"] = productId
                product["sellerId"] = productSellerId
                product["name"] = productName
                product["price"] = productPrice
                product["amount"] = 1

                // Zapisywanie nowego rekordu w bazie danych
                newRecord.setValue(product)
                    .addOnSuccessListener {
                        // Rekord został pomyślnie dodany do bazy danych
                        Toast.makeText(this@ProductDetailsActivity, "Produkt został dodany do koszyka.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        // Wystąpił błąd podczas dodawania rekordu do bazy danych
                        Toast.makeText(this@ProductDetailsActivity, "Wystąpił błąd podczas dodawania produktu do koszyka: " + e.message, Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Użytkownik nie jest zalogowany, obsłuż to odpowiednio
                Toast.makeText(this@ProductDetailsActivity, "Musisz być zalogowany, aby dodać produkt do koszyka.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}