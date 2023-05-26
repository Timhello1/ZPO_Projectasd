package com.example.myapp

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class AdminProductDetailsActivity : AppCompatActivity() {

    private lateinit var productNameTextView: TextView
    private lateinit var productDescriptionTextView: TextView
    private lateinit var productPriceTextView: TextView
    private lateinit var productIngredientsTextView: TextView // dodaj deklarację dla tego pola
    private lateinit var productTagsTextView: TextView // dodaj deklarację dla tego pola
    private lateinit var productPrescriptionTextView: TextView // dodaj deklarację dla tego pola

    private lateinit var productId: String
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_product_details_activity)

        val buttonGoBackFromDetails = findViewById<Button>(R.id.buttonGoBackFromDetails)
        val buttonDelete = findViewById<Button>(R.id.buttonAddToCart)


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
            productPriceTextView.text = product.price
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
        databaseReference = FirebaseDatabase.getInstance().reference.child("products").child(productId)

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

                productNameTextView.text = productName ?: "Nazwa nieznana"
                productDescriptionTextView.text = productDescription ?: "Opis niedostępny"
                productPriceTextView.text = productPrice ?: "Cena nieznana"
                // dodaj TextView dla każdego pola
                // np. val ingredientsTextView = findViewById(R.id.product_ingredients)
                productIngredientsTextView.text = productIngredients ?: "Skład nieznany"
                productTagsTextView.text = productTags ?: "Tagi nieznane"
                productPrescriptionTextView.text = if (productPrescription == true) "Wymagana recepta" else if (productPrescription == false) "Nie wymagana recepta" else ((productPrescription ?: "Recepta nieznana").toString())

                // analogicznie dla reszty pól
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the database error
            }
        })

        buttonGoBackFromDetails.setOnClickListener { this.onBackPressed() }
        buttonDelete.setOnClickListener {
            // Usuń produkt z bazy danych
            databaseReference.removeValue()
                .addOnSuccessListener {
                    // Wyświetl komunikat o powodzeniu usuwania
                    Toast.makeText(this@AdminProductDetailsActivity, "Produkt został usunięty", Toast.LENGTH_SHORT).show()

                    // Wróć do inwentarzu
                    onBackPressed()
                }
                .addOnFailureListener {
                    // Wyświetl komunikat o błędzie usuwania
                    Toast.makeText(this@AdminProductDetailsActivity, "Błąd podczas usuwania produktu", Toast.LENGTH_SHORT).show()
                }
        }
    }
}