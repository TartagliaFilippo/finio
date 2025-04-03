package com.projects.finio.data.local.entity

import android.os.Build
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "schedules",
    indices = [
        Index(
            value = ["title"],
            unique = true
        )
    ]
)

data class Schedule (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,  // VARCHAR30

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.now().epochSecond
    } else {
        System.currentTimeMillis() / 1000
    },

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.now().epochSecond
    } else {
        System.currentTimeMillis() / 1000
    }
)