package com.example.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.databinding.FragmentDisplayLocalsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FragmentDisplayLocals : Fragment() {

    private var _binding: FragmentDisplayLocalsBinding? = null
    private val binding get() = _binding!!

    private lateinit var localsAdapter: LocalsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisplayLocalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        localsAdapter = LocalsAdapter()

        binding.recyclerViewLocals.apply {
            adapter = localsAdapter
            layoutManager = LinearLayoutManager(context)
        }

        loadLocalsFromFirestore()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadLocalsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val localsCollection = db.collection("locals")

        localsCollection
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val localsList = mutableListOf<Local>()

                for (document in querySnapshot) {
                    val name = document.getString("name")
                    val address = document.getString("address")
                    val time = document.getString("time")

                    if (name != null && address != null && time != null) {
                        val local = Local(name, address, time)
                        localsList.add(local)
                    }
                }

                localsAdapter.setLocals(localsList)
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }
}
