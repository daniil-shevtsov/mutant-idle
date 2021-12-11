package com.daniil.shevtsov.idle.feature.main.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.daniil.shevtsov.idle.R
import com.daniil.shevtsov.idle.application.IdleGameApplication
import com.daniil.shevtsov.idle.databinding.FragmentMainBinding
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewModel
import javax.inject.Inject

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context.applicationContext as IdleGameApplication)
            .appComponent
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            composeView.setContent {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
