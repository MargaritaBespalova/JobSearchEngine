package ru.practicum.android.diploma.filter.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBaseBinding
import ru.practicum.android.diploma.filter.ui.models.BaseFilterScreenState
import ru.practicum.android.diploma.filter.ui.view_models.BaseFilterViewModel
import ru.practicum.android.diploma.root.RootActivity
import ru.practicum.android.diploma.util.thisName
import ru.practicum.android.diploma.util.viewBinding

class BaseFilterFragment : Fragment(R.layout.fragment_filter_base) {
    private val binding by viewBinding<FragmentFilterBaseBinding>()
    private val viewModel: BaseFilterViewModel by viewModels { (activity as RootActivity).viewModelFactory }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initViewModelObserver()
    }
    
    private fun initListeners() {
        with(binding) {
            filterToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            workPlaceContainer.setOnClickListener {
                findNavController().navigate(
                    BaseFilterFragmentDirections.actionFilterBaseFragmentToWorkPlaceFilterFragment()
                )
            }
            amountText.doOnTextChanged { _, _, _, count ->
                if (count == 0) viewModel.setEmptyScreenState()
                else viewModel.setApplyScreenState()
            }
        }
    }
    
    private fun initViewModelObserver() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                viewModel.log(thisName, "uiState.collect { state -> ${state.thisName}")
                when (state) {
                    BaseFilterScreenState.Apply -> showApply()
                    BaseFilterScreenState.Choose -> showChoose()
                    BaseFilterScreenState.Empty -> showEmpty()
                }
            }
        }
    }
    
    private fun showApply() {
        with(binding) {
            chooseBaseFilterBtn.visibility = View.GONE
            bottomContainerToApply.visibility = View.VISIBLE
            amountText.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                AppCompatResources.getDrawable(amountText.context, R.drawable.close_btn),
                null
            )
        }
    }
    
    private fun showChoose() {
        with(binding) {
            chooseBaseFilterBtn.visibility = View.VISIBLE
            bottomContainerToApply.visibility = View.GONE
            amountText.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                AppCompatResources.getDrawable(amountText.context, R.drawable.close_btn),
                null
            )
        }
    }
    
    private fun showEmpty() {
        with(binding) {
            chooseBaseFilterBtn.visibility = View.GONE
            bottomContainerToApply.visibility = View.GONE
            amountText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
    }
}