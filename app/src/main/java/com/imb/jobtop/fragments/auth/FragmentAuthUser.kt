package com.imb.jobtop.fragments.auth

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.imb.jobtop.R
import com.imb.jobtop.fragments.base.BaseFragment

class FragmentAuthUser: BaseFragment(R.layout.fragment_auth_user) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFirebaseAuth()
    }

    private fun initFirebaseAuth() {
        auth = Firebase.auth

    }
}