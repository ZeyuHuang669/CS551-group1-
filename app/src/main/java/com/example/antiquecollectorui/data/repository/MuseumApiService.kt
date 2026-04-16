package com.example.antiquecollectorui.data.repository

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

import com.google.gson.annotations.SerializedName

// Response from /search?q=query
data class MetSearchResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("objectIDs") val objectIds: List<Int>?
)

// Response from /objects/{objectID}
data class MetObjectResponse(
    @SerializedName("objectID") val objectId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("objectName") val objectName: String,
    @SerializedName("culture") val culture: String,
    @SerializedName("period") val period: String,
    @SerializedName("dynasty") val dynasty: String,
    @SerializedName("reign") val reign: String,
    @SerializedName("medium") val medium: String,
    @SerializedName("dimensions") val dimensions: String,
    @SerializedName("objectDate") val objectDate: String,
    @SerializedName("department") val department: String,
    @SerializedName("artistDisplayName") val artistDisplayName: String,
    @SerializedName("primaryImage") val primaryImage: String,
    @SerializedName("primaryImageSmall") val primaryImageSmall: String,
    @SerializedName("objectURL") val objectUrl: String,
    @SerializedName("additionalImages") val additionalImages: List<String>
)

interface MetMuseumApiService {

    // Search for objects by keyword

    @GET("search")
    suspend fun searchObjects(
        @Query("q") query: String,
        @Query("hasImages") hasImages: Boolean = true
    ): MetSearchResponse

    // Get full details of a single object by ID
    @GET("objects/{objectId}")
    suspend fun getObject(
        @Path("objectId") objectId: Int
    ): MetObjectResponse
}
