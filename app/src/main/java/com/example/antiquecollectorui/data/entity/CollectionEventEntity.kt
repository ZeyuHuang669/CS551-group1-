package com.example.antiquecollectorui.data.entity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


    @Entity(
        tableName = "collection_events",
        foreignKeys = [
            ForeignKey(
                entity = AntiqueItemEntity::class,
                parentColumns = ["id"],
                childColumns = ["antiqueId"],
                onDelete = ForeignKey.CASCADE
            )
        ],
        indices = [Index("antiqueId")]
    )
    data class CollectionEventEntity(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val antiqueId: Int,
        val eventType: String,          // stores EventType.name()
        val eventDate: Long,
        val notesJson: String
    )