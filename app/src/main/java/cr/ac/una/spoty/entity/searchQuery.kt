package cr.ac.una.spoty.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_query")
data class SearchQuery(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val query: String
)
