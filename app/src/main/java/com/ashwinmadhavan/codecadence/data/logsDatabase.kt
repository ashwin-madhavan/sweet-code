package com.ashwinmadhavan.codecadence.data

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "logs")
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,  // Autoincremented UID
    val category: String,
    val date: Date,
    val startTime: String,
    val endTime: String,
    val totalHours: Double,
    val notes: String
)

object TimeConverter {
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    @TypeConverter
    @JvmStatic
    fun fromTime(time: String): Date? {
        return timeFormat.parse(time)
    }

    @TypeConverter
    @JvmStatic
    fun toTime(time: Date): String {
        return timeFormat.format(time)
    }
}

@Dao
interface LogDao {

    @Insert
    suspend fun insertLog(log: LogEntity)

    @Update
    suspend fun updateLog(log: LogEntity)

    @Query("SELECT * FROM logs")
    fun getAllLogs(): LiveData<List<LogEntity>>

    @Query("SELECT * FROM logs WHERE id = :logId")
    fun getLogById(logId: Long): LiveData<LogEntity>

    @Query("SELECT SUM(totalHours) FROM logs WHERE category = :category")
    fun getTotalHoursForCategoryLiveData(category: String): LiveData<Double>

    @Query("DELETE FROM logs WHERE id = :logId")
    suspend fun deleteLogById(logId: Long)

    @Query("DELETE FROM logs")
    suspend fun deleteAllLogs()
}


@Database(entities = [LogEntity::class], version = 1)
@TypeConverters(TimeConverter::class)
abstract class LogDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
}