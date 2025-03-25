package com.projects.finio.data.local.entity

import android.os.Build
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["parent_id"],
            onDelete = ForeignKey.CASCADE
        )
                  ],
    indices = [
        Index(
            value = ["title"],
            unique = true
        ),
        Index(
            value = ["parent_id"]
        )
    ]
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,  // VARCHAR(30)

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "parent_id")
    val parentId: Int?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.now().epochSecond
    } else {
        System.currentTimeMillis() / 1000
    }
)