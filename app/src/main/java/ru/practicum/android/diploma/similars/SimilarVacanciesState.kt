package ru.practicum.android.diploma.similars

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed interface SimilarVacanciesState {
    object Empty : SimilarVacanciesState
    data class Content(val list: List<Vacancy>) : SimilarVacanciesState
    data class Offline(val message: String) : SimilarVacanciesState
    data class Error(val message: String) : SimilarVacanciesState
    object Loading : SimilarVacanciesState
}