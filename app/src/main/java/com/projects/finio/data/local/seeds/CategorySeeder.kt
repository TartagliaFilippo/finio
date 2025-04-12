package com.projects.finio.data.local.seeds

import com.projects.finio.data.local.AppDatabase
import com.projects.finio.data.local.entity.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CategorySeeder {
    suspend fun seed(db: AppDatabase) {
        withContext(Dispatchers.IO) {
            val dao = db.categoryDao()
            val categories = listOf(
                Category(
                    title = "Appunti",
                    description = "Categoria predefinita",
                    color = "#FF5722",
                    parentId = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                ),
                Category(
                    title = "Benessere",
                    description = "Categoria aggiunta in v15",
                    color = "#009688",
                    parentId = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
            dao.insertAll(categories.toTypedArray())
        }
    }
}