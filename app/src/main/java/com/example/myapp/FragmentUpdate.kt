package com.example.myapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentAddLocalBinding
import com.example.myapp.databinding.FragmentAddProductBinding
import com.example.myapp.databinding.FragmentClientMenuBinding
import com.example.myapp.databinding.FragmentFirstBinding
import com.example.myapp.databinding.FragmentUpdateBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentUpdate : Fragment() {

    private var _binding: FragmentUpdateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGoBack7.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentUpdate_to_fragmentAdminMenu)
        }

        binding.buttonDeleteMed.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentUpdate_to_fragmentAdminMenu)
            Toast.makeText(requireContext(), "Pozycja usunięta", Toast.LENGTH_SHORT).show()
        }

        binding.buttonupdatehehe.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentUpdate_to_fragmentAdminMenu)
            Toast.makeText(requireContext(), "Pozycja zaktualizowana", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}