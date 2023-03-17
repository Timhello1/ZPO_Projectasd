package com.example.myapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentAdminMenuBinding
import com.example.myapp.databinding.FragmentClientMenuBinding
import com.example.myapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentAdminMenu : Fragment() {

    private var _binding: FragmentAdminMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminMenuBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGoBack3.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAdminMenu_to_FirstFragment)
        }

        binding.buttonAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAdminMenu_to_fragmentAddProduct)
        }

        binding.buttonLogOut2.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAdminMenu_to_SecondFragment)
        }

        binding.buttonArrival.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAdminMenu_to_fragmentNotif)
        }

        binding.buttonAddLocal.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAdminMenu_to_fragmentAddLocal)
        }

        binding.buttonupdate.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAdminMenu_to_fragmentUpdate)
        }

        binding.buttonDelete2.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAdminMenu_to_fragmentDelete)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}