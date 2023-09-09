package ru.practicum.android.diploma.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.root.RootActivity
import ru.practicum.android.diploma.search.domain.models.NetworkError
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.ui.models.SearchScreenState
import ru.practicum.android.diploma.search.ui.view_model.SearchViewModel
import ru.practicum.android.diploma.util.thisName
import ru.practicum.android.diploma.util.viewBinding
import javax.inject.Inject

class SearchFragment : Fragment(R.layout.fragment_search) {

    @Inject lateinit var searchAdapter: SearchAdapter
    private val viewModel: SearchViewModel by viewModels { (activity as RootActivity).viewModelFactory }
    private val binding by viewBinding<FragmentSearchBinding>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as RootActivity).component.inject(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.log(thisName, "onResume()")
//        TODO("Сделать запрос в SharedPrefs на наличие текущих филтров." +
//                "Далее если фильтры есть и строка поиска не пустая -> сделать запрос в сеть и обновить список" +
//            "Если фильтрые есть, но строка поиска пустая -> просто применить фильтр без запроса в сеть"
//                )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.log(thisName, "onViewCreated(view: View, savedInstanceState: Bundle?)")
    
        initListeners()
        initAdapter()
        initViewModelObserver()
        
    }
    
    private fun initViewModelObserver() {
        with(viewModel) {
            viewLifecycleOwner.lifecycle.coroutineScope.launch {
                uiState.collect { screenState ->
                    when(screenState) {
                        is SearchScreenState.Content -> showContent(screenState.count, screenState.jobList)
                        is SearchScreenState.Default -> showDefault()
                        is SearchScreenState.Error -> renderError(screenState.error)
                        is SearchScreenState.Loading -> showLoading()
                    }
                }
            }
        }
    }
    
    private fun showContent(count: Int, jobList: List<Vacancy>) {
        refreshJobList(jobList)
        isScrollingEnabled(true)
        
        with(binding) {
            val context = textFabSearch.context
            
            val fabText = StringBuilder()
            fabText.append(context.getString(R.string.found))
            fabText.append(" ")
            fabText.append(
                context.resources.getQuantityString(
                    R.plurals.vacancies, count, count
                )
            )
            
            textFabSearch.text = fabText.toString()
            
            textFabSearch.visibility = View.VISIBLE
            recycler.visibility = View.VISIBLE
            placeholderImage.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }
    
    private fun showDefault() {
        refreshJobList(emptyList())
        isScrollingEnabled(false)
        
        with(binding) {
            textFabSearch.visibility = View.GONE
            recycler.visibility = View.GONE
            placeholderImage.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
    
    private fun renderError(error: NetworkError) {
        refreshJobList(emptyList())
        isScrollingEnabled(false)
        
        when (error) {
            NetworkError.SEARCH_ERROR -> showEmpty()
            NetworkError.CONNECTION_ERROR -> showConnectionError()
        }
    }
    
    private fun showConnectionError() {
        
        with(binding) {
            val context = textFabSearch.context
            textFabSearch.text = context.getString(R.string.update)
            
            textFabSearch.visibility = View.VISIBLE
            recycler.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }
    
    private fun showEmpty() {
        
        with(binding) {
            val context = textFabSearch.context
            textFabSearch.text = context.getString(R.string.empty_search_error)
            
            textFabSearch.visibility = View.VISIBLE
            recycler.visibility = View.GONE
            placeholderImage.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
    
    private fun showLoading() {
        refreshJobList(emptyList())
        isScrollingEnabled(false)
        
        with(binding) {
            val context = textFabSearch.context
            textFabSearch.text = context.getString(R.string.loading_message)
            textFabSearch.visibility = View.VISIBLE
            recycler.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }
    
    private fun initListeners() {
        viewModel.log(thisName, "initListeners()")

        with(binding) {
            filterBtnToolbar.setOnClickListener {
                findNavController().navigate(
                    resId = R.id.action_searchFragment_to_filterBaseFragment,
                    args = null
                )
            }
            searchInputLayout.endIconDrawable =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_search)
            searchInputLayout.isHintEnabled = false
            
            ietSearch.doOnTextChanged { text, _, _, _ ->
                viewModel.onSearchQueryChanged(text.toString())
                if (text.isNullOrEmpty()) {
                    searchInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
                    searchInputLayout.endIconDrawable =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_search)
                } else {
                    searchInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    searchInputLayout.endIconDrawable =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.ic_clear)
                }
            }
        }
    }
    
    private fun initAdapter() {
        viewModel.log(thisName, "initAdapter()")
        binding.recycler.adapter = searchAdapter
        searchAdapter.onClick = { vacancy -> navigateToDetails(vacancy) }
    }
    
    private fun navigateToDetails(vacancy: Vacancy) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToDetailsFragment(vacancy)
        )
    }
    
    private fun refreshJobList(list: List<Vacancy>) {
        searchAdapter.list = list
        searchAdapter.notifyDataSetChanged()
    }
    
    private fun isScrollingEnabled(isEnable: Boolean) {
        with(binding) {
            val toolbarLayoutParams: AppBarLayout.LayoutParams =
                searchToolbar.layoutParams as AppBarLayout.LayoutParams
            
            if (!isEnable) {
                toolbarLayoutParams.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
            } else {
                toolbarLayoutParams.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            }
            searchToolbar.layoutParams = toolbarLayoutParams
        }
    }
}