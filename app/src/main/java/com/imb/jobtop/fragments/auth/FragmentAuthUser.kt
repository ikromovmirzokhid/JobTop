package com.imb.jobtop.fragments.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.imb.jobtop.R
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.utils.HawkUtils
import com.imb.jobtop.utils.extensions.progressOff
import com.imb.jobtop.utils.extensions.progressOn
import kotlinx.android.synthetic.main.fragment_auth_user.*

class FragmentAuthUser : BaseFragment(R.layout.fragment_auth_user) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFirebaseAuth()
        initListeners()
    }

    private fun initListeners() {
        enterBtn.setOnClickListener {
            checkValidity()
        }

        registrationBtn.setOnClickListener {
            findNavController().navigate(R.id.fragmentRegistrationUser)
        }
    }

    private fun checkValidity() {
        val email = loginEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (email.isNotBlank() && password.isNotBlank()) {
            if (!checkEmail()) {
                loginEditText.error = "Wrong formatted email"
                return
            }
            if (password.length < 6) {
                passwordEditText.error = "Password length should be more than 6 characters"
                return
            }
            signInUser(email, password)
        } else {
            Toast.makeText(
                requireContext(), "Email or Password can not be blank",
                Toast.LENGTH_SHORT
            ).show()
            if (email.isBlank())
                loginEditText.error = "Email must be filled"
            if (password.isBlank())
                passwordEditText.error = "Password must be filled"
        }

    }

    private fun checkEmail() =
        loginEditText.text.toString()
            .matches(Regex("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}\$"))

    private fun signInUser(email: String, password: String) {
        progressOn()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressOff()
                if (task.isSuccessful) {
                    if (task.isSuccessful) {
                        HawkUtils.userLoggedIn = true
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.fragmentMain)
                    }
                } else {
                    Toast.makeText(
                        requireContext(), "Signing In failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun initFirebaseAuth() {
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            findNavController().popBackStack()
            findNavController().navigate(R.id.fragmentMain)
        }
    }
}