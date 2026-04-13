package com.example.antiquecollectorui.data.dao
import androidx.room.*
import com.example.antiquecollectorui.data.entity.CollectionEventEntity
import kotlinx.coroutines.flow.Flow

    @Dao
    interface CollectionEventDao {
        @Query("SELECT * FROM collection_events ORDER BY eventDate DESC")
        fun getAllEvents(): Flow<List<CollectionEventEntity>>

        @Query("SELECT * FROM collection_events WHERE antiqueId = :antiqueId ORDER BY eventDate DESC")
        fun getEventsForItem(antiqueId: Int): Flow<List<CollectionEventEntity>>

        @Insert
        suspend fun insert(event: CollectionEventEntity)
    }
