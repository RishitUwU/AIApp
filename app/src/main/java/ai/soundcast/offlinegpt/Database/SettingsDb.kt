package ai.soundcast.offlinegpt.Database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase


@Entity(tableName = "ai_model")
data class SettingsDb(
    @PrimaryKey val id: Int = 0, // Assuming there's only one setting to store
    val name: String
)

@Dao
interface ModelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: SettingsDb)

    @Query("SELECT * FROM ai_model LIMIT 1")
    suspend fun getCurrentModel(): SettingsDb?
}


@Database(entities = [SettingsDb::class], version = 1, exportSchema = false)
abstract class SettingsDatabase : RoomDatabase() {
    abstract fun modelDao(): ModelDao
}