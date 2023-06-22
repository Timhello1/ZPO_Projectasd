package com.example.myapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FragmentBasketChoices : Fragment() {

    private lateinit var checkBox1: CheckBox
    private lateinit var checkBox2: CheckBox
    private lateinit var checkBox3: CheckBox
    private lateinit var checkBox4: CheckBox

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_basket_choices, container, false)

        checkBox1 = view.findViewById(R.id.radioButton)
        checkBox2 = view.findViewById(R.id.radioButton2)
        checkBox3 = view.findViewById(R.id.radioButton3)
        checkBox4 = view.findViewById(R.id.radioButton4)

        val orderButton: Button = view.findViewById(R.id.buttonOrder)
        orderButton.setOnClickListener {
            navigateToSelectedFragment()
        }

        return view
    }

    private fun navigateToSelectedFragment() {
        val isBox1Checked = checkBox1.isChecked;
        val isBox2Checked = checkBox2.isChecked;
        val isBox3Checked = checkBox3.isChecked;
        val isBox4Checked = checkBox4.isChecked;
        val isBox1NotChecked = !checkBox1.isChecked;
        val isBox2NotChecked = !checkBox2.isChecked;
        val isBox3NotChecked = !checkBox3.isChecked;
        val isBox4NotChecked = !checkBox4.isChecked;

        if (isBox1Checked && isBox3Checked && isBox2NotChecked && isBox4NotChecked) {
            findNavController().navigate(R.id.action_fragmentBasketChoices_to_cardAddressFragment);
        } else if (isBox1Checked && isBox4Checked && isBox2NotChecked && isBox3NotChecked) {
            findNavController().navigate(R.id.action_fragmentBasketChoices_to_cardShopFragment);
        } else if (isBox2Checked && isBox3Checked && isBox1NotChecked && isBox4NotChecked) {
            findNavController().navigate(R.id.action_fragmentBasketChoices_to_transferAddressFragment);
        } else if (isBox2Checked && isBox4Checked && isBox1NotChecked && isBox3NotChecked) {
            findNavController().navigate(R.id.action_fragmentBasketChoices_to_transferShopFragment);
        } else {
            Toast.makeText(requireContext(), "Zaznacz tylko po jednej opcji", Toast.LENGTH_SHORT).show();
        }
    }
}