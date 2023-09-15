package ru.practicum.android.diploma.root

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.app.appComponent
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.di.ViewModelFactory
import ru.practicum.android.diploma.util.thisName
import javax.inject.Inject

class RootActivity : AppCompatActivity() {
    val component by lazy(LazyThreadSafetyMode.NONE) {
        appComponent.activityComponentFactory()
            .create()
    }
    
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: RootViewModel by viewModels { viewModelFactory }

    private val binding by lazy(LazyThreadSafetyMode.NONE) { ActivityRootBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        viewModel.log(thisName, "onCreate() -> Unit")
        
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        
        binding.bottomNavigationView.setupWithNavController(navController)
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.log(
                thisName,
                "addOnDestinationChangedListener { destination = ${destination.label} }"
            )
            when (destination.id) {
                R.id.filterBaseFragment,
                R.id.detailsFragment,
                R.id.similarsVacancyFragment,
                R.id.workPlaceFilterFragment,
                R.id.countryFilterFragment,
                R.id.workPlaceFilterFragment,
                R.id.regionFragment,
                R.id.departmentFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun hideBottomNav() {
        viewModel.log(thisName, "hideBottomNav()")
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNav() {
        viewModel.log(thisName, "showBottomNav()")
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}