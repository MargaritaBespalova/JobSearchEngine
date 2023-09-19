package ru.practicum.android.diploma.filter.domain.impl

import ru.practicum.android.diploma.Logger
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.SelectedFilter
import ru.practicum.android.diploma.util.thisName
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.inject.Inject
import kotlin.concurrent.read
import kotlin.concurrent.write

class FilterInteractorImpl @Inject constructor(
    private val filterRepository: FilterRepository,
    private val logger: Logger
) : FilterInteractor {
    
    private val lock = ReentrantReadWriteLock()
    override suspend fun getSavedFilterSettings(key: String): SelectedFilter {
        lock.read {
            return filterRepository
                .getSaveFilterSettings(key = key)
                .also {
                    logger.log(thisName, "getSelectedData($key: String): SelectedData=$it")
                }
        }
    }
    
    override suspend fun clearFilter(key: String) {
        lock.write { filterRepository.saveFilterSettings(key, SelectedFilter.empty) }
    }

    override suspend fun saveFilterSettings(key: String, data: SelectedFilter) {
        lock.write {
            filterRepository.saveFilterSettings(key = key, selectedFilter = data)
            logger.log(thisName, "saveFilterSettings($key: String, $data: SelectedFilter)")
        }
    }
}