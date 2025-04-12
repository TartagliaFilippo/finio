package com.projects.finio.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            INSERT INTO categories (title, description, color, parentId, createdAt, updatedAt)
            VALUES (
                'Benessere',
                'Categoria aggiunta in v15',
                '#009688',
                NULL,
                strftime('%s', 'now'),
                strftime('%s', 'now')
            )
        """.trimIndent())
    }
}