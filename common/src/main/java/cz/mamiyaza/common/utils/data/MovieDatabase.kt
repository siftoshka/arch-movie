package cz.mamiyaza.common.utils.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Database class for Movie.
 */
@Database(entities = [Movie::class], exportSchema = false, version = 1)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDAO
}