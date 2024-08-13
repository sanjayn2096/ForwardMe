package com.example.forwardme.fragments

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.forwardme.Adapters.CountryCodeAdapter
import com.example.forwardme.Data.Country
import com.example.forwardme.Data.ForwardingViewModel
import com.example.forwardme.R
import com.example.forwardme.utils.Utils

class SetupFragment : Fragment(R.layout.fragment_setup) {
    private lateinit var countryCodeSpinner: Spinner
    private lateinit var phoneNumberEditText: EditText
    private lateinit var saveButton: Button

    private val viewModel: ForwardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countryCodeSpinner = view.findViewById(R.id.countryCodeSpinner)
        phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText)
        saveButton = view.findViewById(R.id.saveButton)

        val countries = Utils().getCountries()

        val adapter = CountryCodeAdapter(requireContext(), countries)
        countryCodeSpinner.adapter = adapter

        val setupIsDone = arguments?.getBoolean("setupIsDone") ?: false
        Log.d("SetupFragment", "setup is done in setup fragment = $setupIsDone")
        if (setupIsDone) {
            findNavController().navigate(R.id.action_setupFragment_to_landingFragment)
        } else {
            saveButton.setOnClickListener {
                //TODO: Should get country code where User's sim is registered
                val selectedCountry = countryCodeSpinner.selectedItem as Country
                val phoneNumber = phoneNumberEditText.text.toString()
                if (isValidPhoneNumber(phoneNumber)) {
                    val fullNumber = selectedCountry.code + phoneNumber
                    viewModel.saveForwardNumber(fullNumber, selectedCountry)
                    checkCountryCode(selectedCountry.code)
                    if (setupIsDone) {
                        findNavController().navigate(R.id.action_setupFragment_to_messageFilterFragment)
                    } else {
                        findNavController().navigate(R.id.action_setupFragment_to_landingFragment)
                    }
                } else {
                    Toast.makeText(requireContext(), "Invalid phone number", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        loadSavedPhoneNumber()
    }

    private fun loadSavedPhoneNumber() {
        viewModel.forwardNumber.value?.let { savedNumber ->
            // Default Country Code is being assumed as +1.
            var countryCode = "+1"
            Log.d("SetupFragment" , "Country Code is $countryCode")
            for (i in 0 until countryCodeSpinner.count) {
                val country = countryCodeSpinner.getItemAtPosition(i) as Country
                if (savedNumber.contains(country.code)) {
                    countryCodeSpinner.setSelection(i)
                    countryCode = (countryCodeSpinner.getItemAtPosition(i) as Country).code
                    break
                }
            }
            phoneNumberEditText.setText(savedNumber.removePrefix(countryCode))
        }
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.matches(Regex("[0-9]+"))
    }

    private fun checkCountryCode(inputCountryCode: String) {
        val userCountryCode = getUserCountryCode()

        if (userCountryCode != inputCountryCode) {
            showCountryCodeWarning()
        }
    }

    private fun getUserCountryCode(): String {
        val telephonyManager = requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return "+" + telephonyManager.simCountryIso.toUpperCase()
    }

    private fun showCountryCodeWarning() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Warning")
            .setMessage("The country code of the forwarding number is different from your SIM country code. Additional charges may apply.")
            .setPositiveButton("OK", null)
            .show()
    }
}
