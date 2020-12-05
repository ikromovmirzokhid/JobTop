package com.imb.jobtop.fragments.base


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.imb.jobtop.MainActivity


abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    var detached = true

    fun pressBack() {
        activity?.let { act ->
            if (act is MainActivity) {
                act.onBackPressed()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun getMain(): MainActivity? {
        return activity as MainActivity
    }

    fun closeKeyboard() {
        val inputManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        var view = activity?.currentFocus
        if (view == null && activity != null) {
            view = View(activity)
        }
        val binder = view?.windowToken
        inputManager?.hideSoftInputFromWindow(binder, 0)
    }

    fun closeKeyboard(view: View) {
        val inputManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val binder = view.windowToken
        inputManager?.hideSoftInputFromWindow(binder, 0)
    }

    override fun onDetach() {
        super.onDetach()
        detached = true
        closeKeyboard()
    }
}