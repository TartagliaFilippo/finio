package com.projects.finio.data.local.entity

import android.os.Build
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["category_id"]
        ),
        Index(
            value = ["name"],
            unique = true
        )
    ]
)
data class Item (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "category_id")
    val categoryId: Int?,

    @ColumnInfo(name = "name")
    val name: String,   // VARCHAR(30)

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "subscription")
    val subscription: Boolean = false,

    @ColumnInfo(name = "offer")
    val offer: Boolean = false,

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