package com.example.antiquecollectorui.data.repository

import com.example.antiquecollectorui.data.dao.AntiqueItemDao
import com.example.antiquecollectorui.data.dao.CollectionEventDao
import com.example.antiquecollectorui.data.dao.MuseumCacheDao
import com.example.antiquecollectorui.data.mapper.toDomain
import com.example.antiquecollectorui.data.mapper.toEntity
import com.example.antiquecollectorui.domain.model.AntiqueItem
import com.example.antiquecollectorui.domain.model.CollectionEvent
import com.example.antiquecollectorui.domain.model.MuseumCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AntiqueRepositoryImpl @Inject constructor(
    private val antiqueItemDao: AntiqueItemDao,
    private val collectionEventDao: CollectionEventDao,
    private val museumCacheDao: MuseumCacheDao,
    // private val museumApiService: MuseumApiService
) : AntiqueRepository {

    override fun getAllItems(): Flow<List<AntiqueItem>> =
        antiqueItemDao.getAllItems().map { list -> list.map { it.toDomain() } }

    override suspend fun getItemById(id: Int): AntiqueItem? =
        antiqueItemDao.getItemById(id)?.toDomain()

    override suspend fun insertItem(item: AntiqueItem): Long =
        antiqueItemDao.insert(item.toEntity())

    override suspend fun updateItem(item: AntiqueItem) =
        antiqueItemDao.update(item.toEntity())

    override suspend fun deleteItem(item: AntiqueItem) =
        antiqueItemDao.delete(item.toEntity())

    override fun getAllEvents(): Flow<List<CollectionEvent>> =
        collectionEventDao.getAllEvents().map { list -> list.map { it.toDomain() } }

    override fun getEventsForItem(antiqueId: Int): Flow<List<CollectionEvent>> =
        collectionEventDao.getEventsForItem(antiqueId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertEvent(event: CollectionEvent) =
        collectionEventDao.insert(event.toEntity())

    override suspend fun getCachedMuseumData(museumItemId: String): MuseumCache? =
        museumCacheDao.getByMuseumItemId(museumItemId)?.toDomain()

    override suspend fun insertMuseumCache(cache: MuseumCache) =
        museumCacheDao.insert(cache.toEntity())

    override suspend fun fetchMuseumInfo(query: String): Result<MuseumCache> {
        // TODO: create API service
        return Result.failure(NotImplementedError("Museum API not yet wired"))
    }
}
