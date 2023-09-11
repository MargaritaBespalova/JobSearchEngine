package ru.practicum.android.diploma.filter.ui.view_models

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.Logger
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.NetworkResponse
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.ui.models.FilterScreenState
import ru.practicum.android.diploma.util.thisName
import javax.inject.Inject

class RegionViewModel @Inject constructor(
    private val filterInteractor: FilterInteractor,
    logger: Logger
) : CountryViewModel(filterInteractor, logger) {

    var country: String = ""
    var region: Region? = null

    override fun getData() {
        log("RegionViewModel", "getData()")
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.getRegions().collect { state ->
                when (state) {
                    is NetworkResponse.Error -> {
                        log("RegionViewModel", "NetworkResponse.Error -> ${state.message}")
                        _uiState.value = FilterScreenState.Error(message = state.message)
                    }

                    is NetworkResponse.Offline -> {
                        log("RegionViewModel", "NetworkResponse.Offline -> ${state.message}")
                        _uiState.value = FilterScreenState.Error(message = state.message)
                    }

                    is NetworkResponse.Success -> {
                        log("RegionViewModel", "NetworkResponse.Success -> [${state.data.size}]")
                        _uiState.value = FilterScreenState.Content(state.data)
                    }

                    is NetworkResponse.NoData -> {
                        log("RegionViewModel", "NetworkResponse.NoData -> []")
                        _uiState.value = FilterScreenState.NoData(emptyList<Region>(), message = state.message)
                    }
                }
            }
        }
    }

    private fun getRegions(query: String) {
        log("RegionViewModel", "getRegions($query: String)")
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.getRegions(query).collect { state ->
                when (state) {
                    is NetworkResponse.Error -> {
                        log("RegionViewModel", "NetworkResponse.Error -> ${state.message}")
                        _uiState.value = FilterScreenState.Error(message = state.message)
                    }

                    is NetworkResponse.Offline -> {
                        log("RegionViewModel", "NetworkResponse.Offline -> ${state.message}")
                        _uiState.value = FilterScreenState.Error(message = state.message)
                    }

                    is NetworkResponse.Success -> {
                        log("RegionViewModel", "NetworkResponse.Success -> [${state.data.size}]")
                        _uiState.value = FilterScreenState.Content(state.data)
                    }

                    is NetworkResponse.NoData -> {
                        log("RegionViewModel", "NetworkResponse.NoData -> []")
                        _uiState.value = FilterScreenState.NoData(emptyList<Region>(), message = state.message)
                    }
                }
            }
        }
    }

    override fun hasUserData(fragment: String) {
        if (fragment == REGION_KEY) {
            getCountryFromSharedPref()
            log("RegionViewModel", "country == $country")
            if (country.isEmpty()) getData()
            else getRegions(country)
        }
    }

    private fun getCountryFromSharedPref() {
        log(thisName, "getCountryFromSharedPref()")
        viewModelScope.launch {
            country = filterInteractor.getCountryFromPrefs(COUNTRY_KEY)
        }
    }

    private fun getRegionFromSharedPref() {
        log(thisName, "getRegionFromSharedPref()")
        viewModelScope.launch {
            country = filterInteractor.getCountryFromPrefs(COUNTRY_KEY)
        }
    }

    fun saveRegion(region: String) {
        log(thisName, "saveRegion($region: String)")
        viewModelScope.launch {
            filterInteractor.saveRegion(REGION_KEY, region)
        }
    }

    companion object { const val REGION_KEY = "Region" }
}