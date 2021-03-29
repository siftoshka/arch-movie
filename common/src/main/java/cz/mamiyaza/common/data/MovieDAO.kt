package cz.mamiyaza.common.data

import androidx.room.*

/**
 * DAO interface for Movie.
 */
@Dao
interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun addMovie(movie: Movie)
    @Delete suspend fun deleteMovie(movie: Movie)
    @Transaction @Query("DELETE FROM movie") suspend fun deleteAllMovies()
}

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String
)