package com.example.forwardme

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.forwardme.Adapters.CountryCodeAdapter
import com.example.forwardme.Data.Country
import com.example.forwardme.Data.ForwardingViewModel

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

        val countries = getCountries()
        val adapter = CountryCodeAdapter(requireContext(), countries)
        countryCodeSpinner.adapter = adapter

        saveButton.setOnClickListener {
            //TODO: Should get country code where User's sim is registered
            val selectedCountry = countryCodeSpinner.selectedItem as Country
            val phoneNumber = phoneNumberEditText.text.toString()
            if (isValidPhoneNumber(phoneNumber)) {
                val fullNumber = selectedCountry.code + phoneNumber
                viewModel.saveForwardNumber(fullNumber)
                checkCountryCode(selectedCountry.code)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, LandingFragment())
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "Invalid phone number", Toast.LENGTH_SHORT).show()
            }
        }

        loadSavedPhoneNumber()
    }

    private fun getCountries(): List<Country> {
        return listOf(
            Country("United States", "+1", R.drawable.flag_us),
            Country("India", "+91", R.drawable.flag_in),
            Country("United Kingdom", "+44", R.drawable.flag_uk)
            // Add more countries and their flags here
        )
    }

    private fun loadSavedPhoneNumber() {
        viewModel.forwardNumber.value?.let { savedNumber ->
            val countryCode = savedNumber.takeWhile { it.isDigit() || it == '+' }
            for (i in 0 until countryCodeSpinner.count) {
                val country = countryCodeSpinner.getItemAtPosition(i) as Country
                if (country.code == countryCode) {
                    countryCodeSpinner.setSelection(i)
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
