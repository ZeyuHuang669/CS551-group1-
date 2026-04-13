package com.example.antiquecollectorui.data.mapper

import com.example.antiquecollectorui.data.entity.AntiqueItemEntity
import com.example.antiquecollectorui.data.entity.CollectionEventEntity
import com.example.antiquecollectorui.data.entity.MuseumCacheEntity
import com.example.antiquecollectorui.domain.model.AntiqueItem
import com.example.antiquecollectorui.domain.model.CollectionEvent
import com.example.antiquecollectorui.domain.model.EventType
import com.example.antiquecollectorui.domain.model.MuseumCache

// AntiqueItem
fun AntiqueItemEntity.toDomain() = AntiqueItem(
    id = id, name = name, category = category, description = description,
    estimatedValue = estimatedValue, imageUrl = imageUrl, dateAcquired = dateAcquired,
    condition = condition, notes = notes, provenance = provenance
)

fun AntiqueItem.toEntity() = AntiqueItemEntity(
    id = id, name = name, category = category, description = description,
    estimatedValue = estimatedValue, imageUrl = imageUrl, dateAcquired = dateAcquired,
    condition = condition, notes = notes, provenance = provenance
)

//  CollectionEvent
fun CollectionEventEntity.toDomain() = CollectionEvent(
    id = id, antiqueId = antiqueId,
    eventType = EventType.valueOf(eventType),
    eventDate = eventDate, notesJson = notesJson
)

fun CollectionEvent.toEntity() = CollectionEventEntity(
    id = id, antiqueId = antiqueId,
    eventType = eventType.name,
    eventDate = eventDate, notesJson = notesJson
)

// MuseumCache
fun MuseumCacheEntity.toDomain() = MuseumCache(
    id = id, museumItemId = museumItemId,
    title = title, details = details, imageUrl = imageUrl
)

fun MuseumCache.toEntity() = MuseumCacheEntity(
    id = id, museumItemId = museumItemId,
    title = title, details = details, imageUrl = imageUrl
)