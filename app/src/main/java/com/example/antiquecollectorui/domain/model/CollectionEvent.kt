package com.example.antiquecollectorui.domain.model

data class CollectionEvent(
    val id: Int = 0,
    val antiqueId: Int,
    val eventType: EventType,
    val eventDate: Long = System.currentTimeMillis(),
    val notesJson: String = ""
)