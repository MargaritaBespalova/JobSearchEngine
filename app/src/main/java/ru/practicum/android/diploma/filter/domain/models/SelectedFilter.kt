package ru.practicum.android.diploma.filter.domain.models

data class SelectedFilter(
    val country: Country? = null,
    val region: Region? = null,
    val industry: Industry? = null,
    val salary: String? = null,
    val visibility: Boolean? = null,
) {
    companion object {
       val empty = SelectedFilter()
    }
}