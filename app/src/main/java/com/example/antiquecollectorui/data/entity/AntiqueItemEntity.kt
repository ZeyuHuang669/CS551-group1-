package com.example.antiquecollectorui.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "antique_items")
data class AntiqueItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val description: String,
    val estimatedValue: Double,
    val imageUrl: String,
    val dateAcquired: String,
    val condition: String,
    val notes: String,
    val provenance: String
)