package ru.practicum.android.diploma.filter.ui.models

sealed interface BaseFilterScreenState {
    object Empty : BaseFilterScreenState
    object Choose : BaseFilterScreenState
    object Apply : BaseFilterScreenState
}