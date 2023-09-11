package ru.practicum.android.diploma.search.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vacancy(
    val id: String,
    val iconUri: String = "",
    val title: String = "",
    val company: String = "",
    val salary: String = "",
    val area: String = "",
    val date : String = "",
) : Parcelable



