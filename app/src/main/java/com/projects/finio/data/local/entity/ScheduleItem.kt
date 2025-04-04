package com.projects.finio.data.local.entity

import android.os.Build
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "schedule_items",
    foreignKeys = [
        ForeignKey(
            entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Schedule::class,
            parentColumns = ["id"],
            childColumns = ["schedule_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class ScheduleItem (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "schedule_id")
    val listId: Int,

    @ColumnInfo(name = "item_id")
    val itemId: Int,

    @ColumnInfo(name = "type")
    val type: Int,

    @ColumnInfo(name = "item_quantity")
    val itemQuantity: Int?,

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