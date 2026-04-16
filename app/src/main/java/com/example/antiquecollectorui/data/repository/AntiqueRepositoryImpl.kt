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
    private val metMuseumApiService: MetMuseumApiService
) : AntiqueRepository {

    //  AntiqueItem
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

    //  CollectionEvent
    override fun getAllEvents(): Flow<List<CollectionEvent>> =
        collectionEventDao.getAllEvents().map { list -> list.map { it.toDomain() } }

    override fun getEventsForItem(antiqueId: Int): Flow<List<CollectionEvent>> =
        collectionEventDao.getEventsForItem(antiqueId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertEvent(event: CollectionEvent) =
        collectionEventDao.insert(event.toEntity())

    //  MuseumCache
    override suspend fun getCachedMuseumData(museumItemId: String): MuseumCache? =
        museumCacheDao.getByMuseumItemId(museumItemId)?.toDomain()

    override suspend fun insertMuseumCache(cache: MuseumCache) =
        museumCacheDao.insert(cache.toEntity())

    //  Met Museum API
    override suspend fun fetchMuseumInfo(query: String): Result<MuseumCache> {
        return try {
            // Check cache first
            val cached = museumCacheDao.getByMuseumItemId(query)
            if (cached != null) {
                return Result.success(cached.toDomain())
            }

            // Search the Met API
            val searchResponse = metMuseumApiService.searchObjects(query = query, hasImages = true)

            val objectIds = searchResponse.objectIds
            if (objectIds.isNullOrEmpty()) {
                return Result.failure(Exception("No museum records found for \"$query\""))
            }

            // Get first result with an image
            var museumCache: MuseumCache? = null
            for (id in objectIds.take(5)) {
                val obj = metMuseumApiService.getObject(id)
                if (obj.primaryImageSmall.isNotBlank() || obj.primaryImage.isNotBlank()) {
                    // Build details string from available fields
                    val details = buildString {
                        if (obj.objectName.isNotBlank()) appendLine("Type: ${obj.objectName}")
                        if (obj.culture.isNotBlank()) appendLine("Culture: ${obj.culture}")
                        if (obj.period.isNotBlank()) appendLine("Period: ${obj.period}")
                        if (obj.dynasty.isNotBlank()) appendLine("Dynasty: ${obj.dynasty}")
                        if (obj.medium.isNotBlank()) appendLine("Medium: ${obj.medium}")
                        if (obj.dimensions.isNotBlank()) appendLine("Dimensions: ${obj.dimensions}")
                        if (obj.objectDate.isNotBlank()) appendLine("Date: ${obj.objectDate}")
                        if (obj.artistDisplayName.isNotBlank()) appendLine("Artist: ${obj.artistDisplayName}")
                        if (obj.department.isNotBlank()) appendLine("Department: ${obj.department}")
                    }.trim()

                    museumCache = MuseumCache(
                        museumItemId = query,
                        title = obj.title.ifBlank { "Museum Record" },
                        details = details.ifBlank { "No additional details available." },
                        imageUrl = obj.primaryImageSmall.ifBlank { obj.primaryImage }
                    )
                    break
                }
            }

            if (museumCache == null) {
                return Result.failure(Exception("No museum records with images found for \"$query\""))
            }

            // Cache the result in Room
            museumCacheDao.insert(museumCache.toEntity())

            Result.success(museumCache)

        } catch (e: Exception) {
            Result.failure(Exception("Museum API error: ${e.message}"))
        }
    }
}
