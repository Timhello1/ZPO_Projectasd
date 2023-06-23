package com.example.myapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapp.databinding.FragmentDeleteBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentDelete : Fragment() {

    private var _binding: FragmentDeleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ButtonDelete.setOnClickListener {
            val emailEditText = binding.editTextTextPersonName8
            val passwordEditText = binding.editTextTextPersonName9

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            isAuthorized(email, password) { isAuthorized ->
                if (isAuthorized) {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(requireContext(), "Konto usunięte", Toast.LENGTH_SHORT).show()

                    val user = Firebase.auth.currentUser!!

                    val db = FirebaseFirestore.getInstance()

                    val usersRef = db.collection("users")
                    val userQuery = usersRef.whereEqualTo("email", user.email)
                    userQuery.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
                        if (task.isSuccessful) {
                            for (documentSnapshot in task.result) {
                                documentSnapshot.reference.delete()
                            }
                        }
                    }

                    val adminsRef = db.collection("admins")
                    val adminQuery = adminsRef.whereEqualTo("email", user.email)
                    adminQuery.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
                        if (task.isSuccessful) {
                            for (documentSnapshot in task.result) {
                                documentSnapshot.reference.delete()
                            }
                        }
                    }

                    val ordersRef = db.collection("orders")
                    val ordersQuery = ordersRef.whereEqualTo("name", user.email)
                    ordersQuery.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
                        if (task.isSuccessful) {
                            for (documentSnapshot in task.result) {
                                documentSnapshot.reference.delete()
                            }
                        }
                    }

                    val deliveriesRef = db.collection("finished_deliveries")
                    val deliveriesQuery = deliveriesRef.whereEqualTo("name", user.email)
                    deliveriesQuery.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
                        if (task.isSuccessful) {
                            for (documentSnapshot in task.result) {
                                documentSnapshot.reference.delete()
                            }
                        }
                    }

                    deleteUserOrders(user.email)

                    user.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "User account deleted.")
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isAuthorized(email: String, password: String, callback: (Boolean) -> Unit) {
        val user = Firebase.auth.currentUser
        if (user != null && email == user.email) {
            val credential = EmailAuthProvider.getCredential(email, password)
            user.reauthenticate(credential)
                .addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        // Password matches, invoke the callback with true
                        callback.invoke(true)
                    } else {
                        // Password does not match or re-authentication failed
                        // Invoke the callback with false
                        callback.invoke(false)
                    }
                }
        } else {
            // User is null or email doesn't match
            // Invoke the callback with false
            callback.invoke(false)
        }
    }

    private fun deleteUserOrders(userEmail: String?) {
        val ordersRef = FirebaseDatabase.getInstance().getReference("baskets")
        ordersRef.orderByChild("userName").equalTo(userEmail)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
