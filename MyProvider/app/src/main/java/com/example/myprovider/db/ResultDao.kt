package com.example.myprovider.db

import android.database.Cursor
import androidx.room.*

@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg result: Result)

    @Update
    suspend fun updateResult(vararg result: Result)

    @Query("SELECT * FROM result")
    fun getAll(): Cursor

    @Delete
    suspend fun delete(result: Result)

    @Query("SELECT * FROM result WHERE id= :id")
    suspend fun getItemById(id: Int): List<Result>

    suspend fun insertOrUpdate(result: Result) {
        val id = getItemById(result.id)
        if (id.isEmpty()) {
            insertAll(result)
        } else {
            updateResult(result)
        }
    }
}