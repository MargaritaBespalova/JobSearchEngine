package ru.practicum.android.diploma.search.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegionDto(
    @SerialName("name") val name: String?,
    @SerialName("area") val area: RegionArea?,
)