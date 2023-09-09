package ru.practicum.android.diploma.similars

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarsVacancyBinding
import ru.practicum.android.diploma.root.RootActivity
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.ui.fragment.SearchAdapter
import ru.practicum.android.diploma.util.thisName
import ru.practicum.android.diploma.util.viewBinding
import javax.inject.Inject

class SimilarVacanciesFragment : Fragment(R.layout.fragment_similars_vacancy)  {

    @Inject lateinit var vacancyAdapter: SearchAdapter
    private val viewModel: SimilarVacanciesViewModel by viewModels { (activity as RootActivity).viewModelFactory }
    private val binding by viewBinding<FragmentSimilarsVacancyBinding>()
    private val args by navArgs<SimilarVacanciesFragmentArgs>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as RootActivity).component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.log(thisName, "onViewCreated()")
        initAdapter()
        initListeners()
        collector()
        viewModel.getSimilarVacancies(args.vacancy.id )
    }

    private fun collector() {
        viewModel.log(thisName, "collector()")
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { state ->
                when (state) {
                    is SimilarVacanciesState.Content -> showContent(state.list)
                    is SimilarVacanciesState.Empty -> showEmpty()
                    is SimilarVacanciesState.Error -> showError(state.message)
                    is SimilarVacanciesState.Loading -> showLoading()
                    is SimilarVacanciesState.Offline -> showOffline(state.message)
                }
            }
        }
    }
    
    private fun showContent(list: List<Vacancy>) {
        binding.placeHolder.visibility = View.GONE
        binding.iwAnim.visibility = View.GONE
        binding.recycler.visibility = View.VISIBLE
        val adapter = (binding.recycler.adapter as SearchAdapter)
        adapter.list = list
        adapter.notifyDataSetChanged()
    }
    
    private fun showEmpty() {
        binding.iwAnim.visibility = View.GONE
        binding.recycler.visibility = View.GONE
        binding.placeHolder.visibility = View.VISIBLE
    }
    
    private fun showError(message: String) {
        binding.iwAnim.visibility = View.GONE
        binding.placeHolder.visibility = View.VISIBLE
        binding.placeHolderText.text = message
        Toast
            .makeText(binding.root.context, message, Toast.LENGTH_SHORT)
            .show()
    }
    
    private fun showLoading() {
        binding.iwAnim.visibility = View.VISIBLE
    }
    
    private fun showOffline(message: String) {
        binding.placeHolderText.text = message
        binding.placeHolder.visibility = View.VISIBLE
        Toast
            .makeText(binding.root.context, message, Toast.LENGTH_SHORT)
            .show()
    }
    
    private fun initAdapter() {
        binding.recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = vacancyAdapter
    }
    
    private fun initListeners() {
        vacancyAdapter?.onClick = { vacancy ->
            navigateToDetails(vacancy)
        }
        binding.similarVacancyToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun navigateToDetails(vacancy: Vacancy) {
        findNavController().navigate(
            SimilarVacanciesFragmentDirections.actionSimilarsVacancyFragmentToDetailsFragment(vacancy)
        )
    }
}