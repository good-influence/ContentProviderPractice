package com.example.myprovider.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Result::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun resultDao(): ResultDao

    companion object {
        private const val DATABASE_NAME: String = "result.db"
        private var instance: AppDatabase? = null

        /*
            ?: (Elvis operator) : 좌항이 null 인 경우 default 값 세팅
            also - 객체의 속성을 사용하지 않거나, 변경하지 않는 경우에 사용하는 범위 지정 함수 (Scope Function)
                 - 디버그, 로깅등에 사용
         */
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context.applicationContext).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }
}