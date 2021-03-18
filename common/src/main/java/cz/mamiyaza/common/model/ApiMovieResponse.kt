package cz.mamiyaza.common.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Data classes for Movie.
 */
data class ApiMovieResponse (

    @SerializedName("page") @Expose val page: Int,
    @SerializedName("results") @Expose val results: List<ApiMovieLite>,
    @SerializedName("total_pages") @Expose val totalPages: Int,
    @SerializedName("total_results") @Expose val totalResults: Int
)

@Parcelize
data class ApiMovieLite(
    @SerializedName("id") @Expose val movieId: Int,
    @SerializedName("title") @Expose val movieTitle: String,
    @SerializedName("name") @Expose val showTitle: String,
    @SerializedName("poster_path") @Expose val movieImage: String,
    @SerializedName("backdrop_path") @Expose val backdropPath: String,
    @SerializedName("profile_path") @Expose val starImage: String,
    @SerializedName("release_date") @Expose val releaseDate: String,
    @SerializedName("vote_average") @Expose val voteAverage: Double,
    @SerializedName("first_air_date") @Expose val firstAirDate: Double,
    @SerializedName("media_type") @Expose val mediaType: String
) : Parcelable

@Parcelize
data class ApiMovie(
    @SerializedName("adult") @Expose val adult: Boolean,
    @SerializedName("backdrop_path") @Expose val backdropPath: String,
    @SerializedName("genres") @Expose val movieGenres: @RawValue List<ApiMovieGenre>,
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("imdb_id") @Expose val imdbId: String,
    @SerializedName("original_title") @Expose val originalTitle: String,
    @SerializedName("overview") @Expose val overview: String,
    @SerializedName("popularity") @Expose val popularity: String,
    @SerializedName("poster_path") @Expose val posterPath: String,
    @SerializedName("release_date") @Expose val releaseDate: String,
    @SerializedName("runtime") @Expose val runtime: Int,
    @SerializedName("status") @Expose val status: String,
    @SerializedName("title") @Expose val title: String,
    @SerializedName("vote_average") @Expose val voteAverage: Double,
    @SerializedName("vote_count") @Expose val voteCount: Int,
    @SerializedName("budget") @Expose val budget: Long,
    @SerializedName("revenue") @Expose val revenue: Long
) : Parcelable

data class ApiMovieGenre(
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("name") @Expose val name: String
)