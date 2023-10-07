package com.ashwinmadhavan.codecadence.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity(tableName = "logs")
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,  // Autoincremented ID
    val category: String,
    val date: String,
    val totalHours: Double,
    val notes: String
)

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
    fun getTotalHoursForCategory(category: String): LiveData<Double>

    @Query("SELECT SUM(totalHours) FROM logs")
    fun getTotalHoursLiveData(): LiveData<Double>

    @Query("DELETE FROM logs WHERE id = :logId")
    suspend fun deleteLogById(logId: Long)

    @Query("DELETE FROM logs")
    suspend fun deleteAllLogs()
}


@Database(entities = [LogEntity::class], version = 4)
abstract class LogDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
}