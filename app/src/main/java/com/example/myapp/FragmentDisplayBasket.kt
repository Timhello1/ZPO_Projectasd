package com.example.myapp

import Basket
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.databinding.FragmentDisplayBasketBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FragmentDisplayBasket : Fragment(), BasketAdapter.BasketItemClickListener {

    private var _binding: FragmentDisplayBasketBinding? = null
    private val binding get() = _binding!!

    private lateinit var basketAdapter: BasketAdapter
    private lateinit var valueEventListener: ValueEventListener
    private lateinit var databaseReference: DatabaseReference

    override fun onDeleteButtonClick(position: Int) {
        deleteBasketItem(position)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisplayBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basketAdapter = BasketAdapter(this)
        binding.recyclerViewBasket.apply {
            adapter = basketAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentDisplayBasket_to_fragmentBasketChoices)
        }

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val basketList = mutableListOf<Basket>()

                for (basketSnapshot in dataSnapshot.children) {
                    val basketId = basketSnapshot.key // Retrieve the basketId from the snapshot key
                    val productName = basketSnapshot.child("name").value as String?
                    val productPrice = basketSnapshot.child("price").value as String?
                    val productAmount = basketSnapshot.child("amount").value as Long?

                    if (basketId != null && productName != null && productPrice != null && productAmount != null) {
                        val basket = Basket(basketId, productName, productPrice, productAmount)
                        basketList.add(basket)
                    }
                }

                basketAdapter.setBasket(basketList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Obsłuż błąd odczytu danych
            }
        }

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            databaseReference = FirebaseDatabase.getInstance().reference.child("baskets")
            databaseReference.orderByChild("userId").equalTo(userId).addValueEventListener(valueEventListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // Usuń nasłuchiwanie zmian, gdy fragment jest niszczony
        databaseReference.removeEventListener(valueEventListener)
    }
    private fun deleteBasketItem(position: Int) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val basket = basketAdapter.getBasket(position)
            val basketId = basket.basketId

            val basketRef = FirebaseDatabase.getInstance().reference.child("baskets").child(basketId)
            basketRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Deletion from the database was successful
                } else {
                    // Deletion from the database failed
                }
            }
        }
    }
}
