package ru.practicum.android.diploma.search.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    @SerialName("id") val id: String? = "",
    @SerialName("name") val name: String? = "",
    @SerialName("areas") val areas: List<RegionArea?>
)