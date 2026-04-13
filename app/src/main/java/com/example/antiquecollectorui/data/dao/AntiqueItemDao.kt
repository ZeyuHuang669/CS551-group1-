package com.example.antiquecollectorui.data.dao

import androidx.room.*
import com.example.antiquecollectorui.data.entity.AntiqueItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AntiqueItemDao {
    @Query("SELECT * FROM antique_items ORDER BY name ASC")
    fun getAllItems(): Flow<List<AntiqueItemEntity>>

    @Query("SELECT * FROM antique_items WHERE id = :id")
    suspend fun getItemById(id: Int): AntiqueItemEntity?

    @Query("SELECT * FROM antique_items WHERE category = :category ORDER BY name ASC")
    fun getItemsByCategory(category: String): Flow<List<AntiqueItemEntity>>

    @Query("SELECT * FROM antique_items WHERE name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchItems(query: String): Flow<List<AntiqueItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AntiqueItemEntity): Long

    @Update
    suspend fun update(item: AntiqueItemEntity)

    @Delete
    suspend fun delete(item: AntiqueItemEntity)
}