package cr.ac.una.spoty.db

import androidx.room.Database
import androidx.room.RoomDatabase
import cr.ac.una.spoty.dao.SearchQueryDao
import cr.ac.una.spoty.entity.SearchQuery

@Database(entities = [SearchQuery::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchQueryDao(): SearchQueryDao
}
