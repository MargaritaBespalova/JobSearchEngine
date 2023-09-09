package ru.practicum.android.diploma.search.data.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.practicum.android.diploma.search.data.network.CodeResponse
import ru.practicum.android.diploma.search.data.network.dto.RegionDto

@Serializable
data class RegionCodeResponse (
    @SerialName("results") val results: List<RegionDto>
) : CodeResponse()