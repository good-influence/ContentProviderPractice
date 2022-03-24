package com.example.myprovider.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "result")
data class Result(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,

    @ColumnInfo(name = "result") val result: String,

    @ColumnInfo(name = "date") val date: String
)
