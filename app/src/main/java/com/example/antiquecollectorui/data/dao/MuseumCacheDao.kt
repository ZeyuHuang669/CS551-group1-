package com.example.antiquecollectorui.data.dao
import androidx.room.*
import com.example.antiquecollectorui.data.entity.MuseumCacheEntity


    @Dao
    interface MuseumCacheDao {
        @Query("SELECT * FROM museum_cache WHERE museumItemId = :museumItemId LIMIT 1")
        suspend fun getByMuseumItemId(museumItemId: String): MuseumCacheEntity?

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(cache: MuseumCacheEntity)
    }