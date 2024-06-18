package com.example.assignmentindividu.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.assignmentindividu.ClosingPage
import com.example.assignmentindividu.R
import com.example.assignmentindividu.`object`.Data

class ProfileFragment : Fragment() {

    private lateinit var usernameTV: TextView
    private lateinit var emailTV: TextView
    private lateinit var phoneNumberTV: TextView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usernameTV = view.findViewById(R.id.usernameText)
        emailTV = view.findViewById(R.id.emailText)
        phoneNumberTV = view.findViewById(R.id.phoneNumberText)
        logoutButton = view.findViewById(R.id.logoutButton)

        usernameTV.text = Data.myProfile?.username
        emailTV.text = Data.myProfile?.email
        phoneNumberTV.text = Data.myProfile?.telephoneNumber


        logoutButton.setOnClickListener {
            val intent = Intent(requireContext(),ClosingPage::class.java)
            Data.myProfile = null
            startActivity(intent)
        }
    }
}