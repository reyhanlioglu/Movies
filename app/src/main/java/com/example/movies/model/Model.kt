package com.example.movies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class Response(
    val results: List<Movie>
)

@Entity // Can be added (tableName="")
data class Movie (
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val movieId: String?,

    @ColumnInfo(name = "name")
    @SerializedName("title")
    val movieName: String?,

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    val popularity: String?,

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    val voteAverage: String?,

    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    val language: String?,

    @ColumnInfo(name = "image_path")
    @SerializedName("poster_path")
    val imagePath: String?

) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}


data class MoviePalette(var color: Int)


data class SmsInfo(
    var to: String,
    var text: String,
    var imageUrl: String?
)
