package ru.practicum.android.diploma.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.root.RootActivity
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.ui.models.SearchUiState
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
                    val painter = SearchScreenPainter(binding)
                    when (screenState) {
                        is SearchUiState.Content -> {
                            log(thisName, "screenState.list = ${screenState.list}")
                            log(thisName, "screenState.isLastPage = ${screenState.isLastPage}")
                            searchAdapter.submitList(screenState.list)
                            searchAdapter.isLastPage(screenState.isLastPage)
                            
                            painter.showContent(screenState.found)
                        }
                        is SearchUiState.AddedContent -> {
                           val newList = searchAdapter.currentList + screenState.list
                            searchAdapter.submitList(newList)
                            searchAdapter.isLastPage(screenState.isLastPage)
                            painter.showContent(screenState.found)
                        }
                        is SearchUiState.Default -> { painter.showDefault() }
                        is SearchUiState.Error -> { painter.renderError(screenState.error) }
                        is SearchUiState.ErrorScrollLoading -> { painter.renderErrorScrolling(screenState.error) }
                        is SearchUiState.Loading -> { painter.showLoading() }
                    }
                }
            }
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
            
            viewModel.log(thisName, "endIconDrawable = ${searchInputLayout.endIconDrawable}")
            
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
            
            btnUpdate.setOnClickListener {
                viewModel.searchVacancies(ietSearch.text.toString())
            }
            
            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    
                    if (dy > 0) {
                        val pos = (recycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        val itemsCount = searchAdapter.itemCount
                        if (pos >= itemsCount - 5) {
                            viewModel.onScrolledBottom()
                        }
                    }
                }
            })
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
}