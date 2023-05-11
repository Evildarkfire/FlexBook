package com.aberon.flexbook.store

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aberon.flexbook.model.*

@Database(
    entities = [Book::class, BookParametr::class, Author::class, Email::class, Cover::class, Preference::class],
    version = 1
)
abstract class SQLDataBase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
}