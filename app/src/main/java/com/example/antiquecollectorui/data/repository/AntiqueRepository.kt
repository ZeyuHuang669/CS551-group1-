package com.example.antiquecollectorui.data.repository

import com.example.antiquecollectorui.domain.model.AntiqueItem
import com.example.antiquecollectorui.domain.model.CollectionEvent
import com.example.antiquecollectorui.domain.model.MuseumCache
import kotlinx.coroutines.flow.Flow



    interface AntiqueRepository {
        // --- AntiqueItem operations ---
        fun getAllItems(): Flow<List<AntiqueItem>>
        suspend fun getItemById(id: Int): AntiqueItem?
        suspend fun insertItem(item: AntiqueItem): Long
        suspend fun updateItem(item: AntiqueItem)
        suspend fun deleteItem(item: AntiqueItem)

        // --- CollectionEvent operations ---
        fun getAllEvents(): Flow<List<CollectionEvent>>
        fun getEventsForItem(antiqueId: Int): Flow<List<CollectionEvent>>
        suspend fun insertEvent(event: CollectionEvent)

        // --- MuseumCache operations ---
        suspend fun getCachedMuseumData(museumItemId: String): MuseumCache?
        suspend fun insertMuseumCache(cache: MuseumCache)

        // --- Museum API ---
        suspend fun fetchMuseumInfo(query: String): Result<MuseumCache>
    }