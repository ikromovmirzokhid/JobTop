package com.imb.jobtop.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.navigation.fragment.findNavController
import com.imb.jobtop.R
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.utils.HawkUtils

class FragmentSplash : BaseFragment(R.layout.fragment_splash_screen) {

    private var counter: CountDownTimer? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader()
    }

    private fun loader() {

        counter = object : CountDownTimer(500, 500) {
            override fun onFinish() {
                if (HawkUtils.userLoggedIn) {
//                    findNavController().navigate(R.id.)
                } else {
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.fragmentAuthUser)
                }
            }

            override fun onTick(millisUntilFinished: Long) {
            }
        }.start()
    }
}