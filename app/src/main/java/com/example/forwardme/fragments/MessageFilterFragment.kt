package com.example.forwardme.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.forwardme.Adapters.CountryCodeAdapter
import com.example.forwardme.Data.Country
import com.example.forwardme.Data.ForwardingViewModel
import com.example.forwardme.R
import com.example.forwardme.utils.Utils


class MessageFilterFragment : Fragment(R.layout.fragment_message_filter) {

    private lateinit var fromNumberEditText: EditText
    private lateinit var keywordsEditText: EditText
    private lateinit var criteriaSpinner: Spinner
    private lateinit var saveFilterButton: Button
    private lateinit var countryCodeSpinner: Spinner

    private val viewModel: ForwardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = context?.getSharedPreferences("ForwardingPrefs", Context.MODE_PRIVATE)
        val getNumber = prefs?.getString("fromNumber", "")
        val getKeywords = prefs?.getString("keywords", "")
        val getCriteria = prefs?.getString("criteria", "")
        val getCountryCode = prefs?.getString("countryCode", "")

        val setupIsDone = (getNumber != "default")

        countryCodeSpinner = view.findViewById(R.id.countryCodeSpinner2)
        fromNumberEditText = view.findViewById(R.id.phoneNumberEditText)
        keywordsEditText = view.findViewById(R.id.keywordsEditText)
        criteriaSpinner = view.findViewById(R.id.andOrSpinner)
        saveFilterButton = view.findViewById(R.id.saveFilterButton)

        val countries = Utils().getCountries()
        val countryAdapter = CountryCodeAdapter(requireContext(), countries)
        countryCodeSpinner.adapter = countryAdapter

        val criteriaOptions = listOf("AND", "OR")
        val criteriaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, criteriaOptions)
        criteriaSpinner.adapter = criteriaAdapter


        if (getKeywords != "") {
            val editableKeywords = Editable.Factory.getInstance().newEditable(getKeywords)
            keywordsEditText.text = editableKeywords
        }

        if (getNumber != "") {
            val editableNumber = Editable.Factory.getInstance().newEditable(getNumber)
            fromNumberEditText.text = editableNumber
        }

        if (getCriteria != "") {
            val criteriaSpinnerPosition= criteriaAdapter.getPosition(getCriteria)
            criteriaSpinner.setSelection(criteriaSpinnerPosition)
        }

        if (getCountryCode != "") {
            //val countryCodeSpinnerPosition= countryAdapter.getPosition(Country())
            //criteriaSpinner.setSelection(countryCodeSpinnerPosition)
        }

        saveFilterButton.setOnClickListener {
            val fromNumber = fromNumberEditText.text.toString()
            val keywords = keywordsEditText.text.toString()
            val criteria = criteriaSpinner.selectedItem.toString()
            val countryCode = countryCodeSpinner.selectedItem.toString()
            viewModel.saveFilterCriteria(fromNumber, keywords, criteria, countryCode)
            findNavController().navigate(R.id.action_messageFilterFragment_to_landingFragment)
        }
    }
}
