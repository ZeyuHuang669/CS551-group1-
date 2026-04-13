package com.example.antiquecollectorui.domain.model

data class AntiqueItem(
    val id: Int = 0,
    val name: String,
    val category: String,
    val description: String = "",
    val estimatedValue: Double = 0.0,
    val imageUrl: String = "",
    val dateAcquired: String = "",
    val condition: String = "",
    val notes: String = "",
    val provenance: String = ""
)