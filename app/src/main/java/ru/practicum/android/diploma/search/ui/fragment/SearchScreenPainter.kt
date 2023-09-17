package ru.practicum.android.diploma.search.ui.fragment

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.appbar.AppBarLayout
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.util.functional.Failure
import ru.practicum.android.diploma.util.thisName

class SearchScreenPainter(
    private val binding: FragmentSearchBinding,
) {
    
    private val context = binding.root.context
    
    fun showDefault() {
        isScrollingEnabled(false)
        
        with(binding) {
            textFabSearch.visibility = View.GONE
            recycler.visibility = View.GONE
            placeholderImage.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            btnUpdate.visibility = View.GONE
        }
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
    
    fun showLoading() {
        isScrollingEnabled(false)
        
        with(binding) {
            hideKeyboard(ietSearch)
            textFabSearch.text = context.getString(R.string.loading_message)
            textFabSearch.visibility = View.VISIBLE
            recycler.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            btnUpdate.visibility = View.GONE
        }
    }
    
    fun showContent(found: Int) {
        isScrollingEnabled(true)
        
        with(binding) {
            val fabText = StringBuilder()
            fabText.append(context.getString(R.string.found))
            fabText.append(" ")
            fabText.append(
                context.resources.getQuantityString(
                    R.plurals.vacancies, found, found
                )
            )
            
            textFabSearch.text = fabText.toString()
            textFabSearch.visibility = View.VISIBLE
            recycler.visibility = View.VISIBLE
            placeholderImage.visibility = View.GONE
            progressBar.visibility = View.GONE
            btnUpdate.visibility = View.GONE
        }
    }
    
    fun renderError(failure: Failure) {
        isScrollingEnabled(false)
        Log.d(thisName, "render: $failure")
        
        when (failure) {
            is Failure.Offline -> showConnectionError()
            else -> showEmpty()
        }
    }
    
    fun renderErrorScrolling(failure: Failure) {
        isScrollingEnabled(true)
        Log.d(thisName, "renderErrorScrolling: $failure")
        
        when (failure) {
            is Failure.Offline -> showToast(R.string.no_internet_message)
            else -> showToast(R.string.server_error)
            
        }
    }
    
    private fun showConnectionError() {
        with(binding) {
            textFabSearch.text = context.getString(R.string.no_internet_message)
            textFabSearch.visibility = View.VISIBLE
            recycler.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            btnUpdate.visibility = View.VISIBLE
        }
    }
    
    private fun showEmpty() {
        
        with(binding) {
            textFabSearch.text = context.getString(R.string.empty_search_error)
            textFabSearch.visibility = View.VISIBLE
            recycler.visibility = View.GONE
            placeholderImage.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            btnUpdate.visibility = View.GONE
            
        }
    }
    
    private fun showToast(@StringRes stringRes: Int) {
        Toast
            .makeText(context, stringRes, Toast.LENGTH_SHORT)
            .show()
    }
    
    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
