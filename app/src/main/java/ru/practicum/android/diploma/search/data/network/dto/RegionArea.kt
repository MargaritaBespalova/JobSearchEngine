package ru.practicum.android.diploma.search.data.network.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Parcelize
@Serializable
data class RegionArea(
    @SerialName("id") val id: String? = "",
    @SerialName("parent_id") val parentId: String? = "",
    @SerialName("name") val name: String? = "",
    @SerialName("areas") val areas: List<RegionArea?>,
) : Parcelable