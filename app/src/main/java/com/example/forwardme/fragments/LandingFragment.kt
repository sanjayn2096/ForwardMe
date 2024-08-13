package com.example.forwardme.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.forwardme.Data.ForwardingViewModel
import com.example.forwardme.R

class LandingFragment : Fragment(R.layout.fragment_landing) {

    private lateinit var forwardingNumberTextView: TextView
    private lateinit var numberDisplayBox: TextView
    private lateinit var changeNumberButton: Button
    private lateinit var changeMessageFilter: Button

    private val viewModel: ForwardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forwardingNumberTextView = view.findViewById(R.id.forwardingNumberTextView)
        numberDisplayBox = view.findViewById(R.id.numberDisplayView)
        changeNumberButton = view.findViewById(R.id.changeNumberButton)
        changeMessageFilter = view.findViewById(R.id.changeFilterButton)

        viewModel.forwardNumber.observe(viewLifecycleOwner) { savedNumber ->
            numberDisplayBox.text = savedNumber
        }

        changeNumberButton.setOnClickListener {
            Log.e("LandingFragment", "Change Number Being Clicked")
            findNavController().navigate(R.id.action_landingFragment_to_setupFragment)
        }

        changeMessageFilter.setOnClickListener{
            findNavController().navigate(R.id.action_landingFragment_to_messageFilterFragment)
        }
    }
}
