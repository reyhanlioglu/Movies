package com.example.movies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class Response(
    val results: List<Movie>
    // val resultsTopRated: List<TopRatedMovie>
)

@Entity // Can be added (tableName="")
data class Movie(
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val movieId: String?,

    @ColumnInfo(name = "name")
    @SerializedName("title")
    val movieName: String?,

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    val overview: String?,

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    val releaseDate: String?,

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    val voteAverage: String?,

    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    val language: String?,

    @ColumnInfo(name = "image_path")
    @SerializedName("poster_path")
    val imagePath: String?,

    @ColumnInfo(name = "type")
    var movieType: String?

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


@Entity // Can be added (tableName="")
data class FavouriteMovie(
    @ColumnInfo(name = "id")
    //  @SerializedName("id")
    var movieId: String?,

    @ColumnInfo(name = "name")
    //   @SerializedName("title")
    var movieName: String?,

    @ColumnInfo(name = "overview")
    //   @SerializedName("overview")
    var overview: String?,

    @ColumnInfo(name = "release_date")
    //   @SerializedName("release_date")
    var releaseDate: String?,

    @ColumnInfo(name = "vote_average")
    //   @SerializedName("vote_average")
    var voteAverage: String?,

    @ColumnInfo(name = "original_language")
    //   @SerializedName("original_language")
    var language: String?,

    @ColumnInfo(name = "image_path")
    //   @SerializedName("poster_path")
    var imagePath: String?,

    @ColumnInfo(name = "type")
    var movieType: String?
) {

    constructor(movie: Movie) : this(
        movie.movieId,
        movie.movieName,
        movie.overview,
        movie.releaseDate,
        movie.voteAverage,
        movie.language,
        movie.imagePath,
        movie.movieType
    )

    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}