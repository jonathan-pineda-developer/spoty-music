package cr.ac.una.spoty.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cr.ac.una.spoty.entity.SearchQuery

@Dao
interface SearchQueryDao {
    @Insert
    fun insertSearchQuery(searchQuery: SearchQuery)

    @Query("SELECT * FROM search_query")
    fun getAllSearchQueries(): List<SearchQuery>
}



