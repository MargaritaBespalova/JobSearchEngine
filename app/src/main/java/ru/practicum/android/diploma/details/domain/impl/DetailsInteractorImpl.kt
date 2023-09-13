package ru.practicum.android.diploma.details.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.details.domain.DetailsRepository
import ru.practicum.android.diploma.details.domain.DetailsInteractor
import ru.practicum.android.diploma.details.domain.models.VacancyFullInfo
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.functional.Either
import ru.practicum.android.diploma.util.functional.Failure
import javax.inject.Inject

class DetailsInteractorImpl@Inject constructor(
    private val repository: DetailsRepository
) : DetailsInteractor {

    override suspend fun getFullVacancyInfoById(id: String): Either<Failure, VacancyFullInfo> {
        return repository.getFullVacancyInfo(id)
    }

    override suspend fun getSimilarVacancies(id: String): Either<Failure, List<Vacancy>> {
        return repository.getSimilarVacancies(id)
    }

    override suspend fun addVacancyToFavorites(vacancy: VacancyFullInfo): Flow<Unit> {
        return repository.addVacancyToFavorite(vacancy)
    }

    override suspend fun removeVacancyFromFavorite(id: String): Flow<Int> {
        return repository.removeVacancyFromFavorite(id)
    }

    override suspend fun showIfInFavourite(id: String): Flow<Boolean> {
        return repository.showIfInFavourite(id)
    }

    override suspend fun getFavoritesById(id: String): Flow<VacancyFullInfo> {
        return repository.getFavoritesById(id)
    }
}