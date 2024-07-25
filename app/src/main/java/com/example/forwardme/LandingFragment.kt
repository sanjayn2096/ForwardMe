package com.example.forwardme

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.forwardme.Data.ForwardingViewModel

class LandingFragment : Fragment(R.layout.fragment_landing) {

    private lateinit var forwardingNumberTextView: TextView
    private lateinit var numberDisplayBox: TextView
    private lateinit var changeNumberButton: Button

    private val viewModel: ForwardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forwardingNumberTextView = view.findViewById(R.id.forwardingNumberTextView)
        numberDisplayBox = view.findViewById(R.id.numberDisplayView)
        changeNumberButton = view.findViewById(R.id.changeNumberButton)

        viewModel.forwardNumber.observe(viewLifecycleOwner) { savedNumber ->
            numberDisplayBox.text = savedNumber
        }

        changeNumberButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, SetupFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
