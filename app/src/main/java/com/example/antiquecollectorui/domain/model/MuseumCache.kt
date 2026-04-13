package com.example.antiquecollectorui.domain.model


    data class MuseumCache(
        val id: Int = 0,
        val museumItemId: String,
        val title: String,
        val details: String,
        val imageUrl: String
    )
