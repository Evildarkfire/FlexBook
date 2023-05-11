package com.aberon.flexbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preference")
data class Preference(
    @PrimaryKey
    var preferenceId: PreferenceKey,
    @ColumnInfo(index = true)
    var preferenceValue: Int
)