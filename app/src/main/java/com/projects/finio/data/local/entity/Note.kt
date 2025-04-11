package com.projects.finio.data.local.entity

import android.os.Build
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Period::class,
            parentColumns = ["id"],
            childColumns = ["period_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "category_id")
    val categoryId: Int,

    @ColumnInfo(name = "period_id")
    val periodId: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "expire_date")
    val expireDate: Long?,

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