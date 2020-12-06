package com.imb.jobtop.fragments.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imb.jobtop.R
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.utils.HawkUtils
import com.imb.jobtop.utils.extensions.progressOff
import com.imb.jobtop.utils.extensions.progressOn
import com.imb.jobtop.utils.extensions.showError
import kotlinx.android.synthetic.main.fragment_registration_user.*

class FragmentRegistrationUser : BaseFragment(R.layout.fragment_registration_user) {

    private lateinit var auth: FirebaseAuth
    private val db = lazy { Firebase.firestore }.value


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFirebase()
        initListeners()
    }

    private fun initListeners() {
        enterBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        registerUserBtn.setOnClickListener {
            checkValidity()
        }
    }

    private fun initFirebase() {
        auth = Firebase.auth
    }

    private fun createUser(email: String, password: String, user: HashMap<String, out Any>) {
        println("Debugging123 - $user")
        progressOn()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            progressOff()
            if (task.isSuccessful) {
                HawkUtils.userLoggedIn = true
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        println("Debugging123 - $documentReference")
                    }
                    .addOnFailureListener { e ->
                        println("Debugging123 - ${e.message}")
                        showError(e.message)
                    }
                Toast.makeText(
                    requireContext(), "Successfully Signed In",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
                findNavController().navigate(R.id.fragmentMain)
            } else {
                println("Debugging123 - ${task.exception}")
                showError(task.exception.toString())
                Toast.makeText(
                    requireContext(), "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkValidity() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val rePassword = rePasswordEditText.text.toString()
        val interests = interestEditText.text.toString()

        if (email.isNotBlank() && password.isNotBlank() && name.isNotBlank() &&
            rePassword.isNotBlank() && interests.isNotBlank()
        ) {
            if (!checkEmail()) {
                emailEditText.error = "Wrong formatted email"
                return
            }
            if (password.length < 6) {
                passwordEditText.error = "Password length should be more than 6 characters"
                return
            }
            if (rePassword != password) {
                rePasswordEditText.error = "Password is not compatible"
                return
            }
            val user = hashMapOf(
                "name" to name,
                "interests" to interests,
            )
            createUser(email, password, user)
        } else {
            Toast.makeText(
                requireContext(), "Every field must be filled up",
                Toast.LENGTH_SHORT
            ).show()
            if (email.isBlank())
                emailEditText.error = "Email must be filled"
            if (password.isBlank())
                passwordEditText.error = "Password must be filled"
            if (name.isBlank())
                nameEditText.error = "Password must be filled"
            if (rePassword.isBlank())
                rePasswordEditText.error = "Password must be filled"
            if (interests.isBlank())
                interestEditText.error = "Interests must be filled"
        }

    }

    private fun checkEmail() =
        emailEditText.text.toString()
            .matches(Regex("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}\$"))

}