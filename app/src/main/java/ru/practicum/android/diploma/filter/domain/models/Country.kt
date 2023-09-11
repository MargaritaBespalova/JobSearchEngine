package ru.practicum.android.diploma.filter.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.practicum.android.diploma.search.data.network.dto.RegionArea

@Parcelize
data class Country(
    val id: String = "-1",
    val name: String = "",
    val area : List<RegionArea?>
): Parcelable