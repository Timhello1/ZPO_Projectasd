package com.example.myapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentClientMenuBinding
import com.example.myapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ClientMenuFragment : Fragment() {

    private var _binding: FragmentClientMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentClientMenuBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonShopping.setOnClickListener {
            findNavController().navigate(R.id.action_clientMenuFragment_to_FragmentInventory)
        }

        binding.buttonGoBack2.setOnClickListener {
            findNavController().navigate(R.id.action_clientMenuFragment_to_FirstFragment)
        }

        binding.buttonDelete.setOnClickListener {
            findNavController().navigate(R.id.action_clientMenuFragment_to_fragmentDelete)
        }
        binding.buttonLocals.setOnClickListener {
            findNavController().navigate(R.id.action_clientMenuFragment_to_fragmentDisplayLocals)
        }

        binding.buttonShowBasket.setOnClickListener{
            findNavController().navigate(R.id.action_clientMenuFragment_to_FragmentDisplayBasket)

        }
        binding.buttonSchedule.setOnClickListener {
            findNavController().navigate(R.id.action_clientMenuFragment_to_schedule_Fragment)
        }
        binding.buttonLogOut.setOnClickListener {
            findNavController().navigate(R.id.action_clientMenuFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}