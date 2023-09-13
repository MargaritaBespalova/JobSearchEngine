package ru.practicum.android.diploma.details.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.details.data.local.db.FavoriteDao
import ru.practicum.android.diploma.details.data.local.model.VacancyConverter
import ru.practicum.android.diploma.details.domain.models.VacancyFullInfo
import javax.inject.Inject

class LocalDataSourceDetailsImpl @Inject constructor(
    private val dao: FavoriteDao,
    private val converter: VacancyConverter
) : LocalDataSource {

    override suspend fun removeVacancyFromFavorite(id: String): Flow<Int> {
        return flowOf(dao.delete(id))
    }

    override suspend fun addVacancyToFavorite(vacancy: VacancyFullInfo): Flow<Unit> {
        return flowOf(dao.insert(converter.toFullInfoEntity(vacancy)))
    }

    override suspend fun showIfInFavouriteById(id: String): Flow<Boolean> {
        return dao.showIfInFavouriteById(id).map { it }
    }

    override suspend fun isVacancyInFavs(id: String): Boolean {
        return dao.isVacancyInFavs(id)
    }
    override suspend fun getFavoritesById(id: String): Flow<VacancyFullInfo> {
        return dao.getFavoritesById(id).map { converter.entityToModel(it) }
    }
}