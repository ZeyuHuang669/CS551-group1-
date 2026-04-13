package com.example.antiquecollectorui.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = "museum_cache")
    data class MuseumCacheEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val museumItemId: String,
        val title: String,
        val details: String,
        val imageUrl: String
    )