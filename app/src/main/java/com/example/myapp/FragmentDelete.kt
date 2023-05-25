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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentDelete : Fragment() {

    private var _binding: FragmentDeleteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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
            val intent = Intent(context,LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(requireContext(), "Konto usunięte", Toast.LENGTH_SHORT).show()
            val user = Firebase.auth.currentUser!!
            val email = user?.email

            val db = FirebaseFirestore.getInstance()


            // Delete user document from 'users' collection
            val usersRef = db.collection("users")
            val userQuery = usersRef.whereEqualTo("email", user.email)
            userQuery.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    for (documentSnapshot in task.result) {
                        documentSnapshot.reference.delete()
                    }
                }
            }

            // Delete user document from 'admins' collection

            // Delete user document from 'admins' collection
            val adminsRef = db.collection("admins")
            val adminQuery = adminsRef.whereEqualTo("email", user.email)
            adminQuery.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    for (documentSnapshot in task.result) {
                        documentSnapshot.reference.delete()
                    }
                }
            }

            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User account deleted.")
                    }
                }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}