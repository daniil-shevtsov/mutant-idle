package com.daniil.shevtsov.idle.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.compose.material.Text
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.daniil.shevtsov.idle.R
import com.daniil.shevtsov.idle.core.IdleGameApplication
import com.daniil.shevtsov.idle.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            composeView.setContent {
                Text("Hello")
            }
        }
    }
}
