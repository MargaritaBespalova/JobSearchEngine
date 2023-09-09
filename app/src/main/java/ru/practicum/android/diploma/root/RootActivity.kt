package ru.practicum.android.diploma.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.app.App
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.di.ViewModelFactory
import javax.inject.Inject

class RootActivity : AppCompatActivity() {
    val component by lazy(LazyThreadSafetyMode.NONE) {
        (application as App).component
            .activityComponentFactory()
            .create()
    }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val binding by lazy(LazyThreadSafetyMode.NONE) { ActivityRootBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        
        setContentView(binding.root)
        
        
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        
        binding.bottomNavigationView.setupWithNavController(navController)
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.filterBaseFragment,
                R.id.detailsFragment,
                R.id.similarsVacancyFragment,
                R.id.workPlaceFilterFragment,
                R.id.countryFilterFragment,
                R.id.workPlaceFilterFragment,
                -> hideBottomNav()
                
                else -> showBottomNav()
            }
        }
    }
    
    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}