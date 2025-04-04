package com.projects.finio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projects.finio.data.local.entity.Subscription

@Dao
interface SubscriptionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSubscription(subscription: Subscription)

    @Query("DELETE FROM subscriptions WHERE item_id = :itemId")
    suspend fun deleteAllSubscriptionsFromItem(itemId: Int)
}