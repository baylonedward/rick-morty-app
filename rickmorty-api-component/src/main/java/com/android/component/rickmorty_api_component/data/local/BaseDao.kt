package com.android.component.rickmorty_api_component.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */
interface BaseDao<T> {
  /**
   * Insert an object in the database.
   *
   * @param obj the object to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(obj: T)

  /**
   * Insert an array of objects in the database.
   *
   * @param obj the objects to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(obj: List<T>)

  /**
   * Update an object from the database.
   *
   * @param obj the object to be updated
   */
  @Update
  suspend fun update(obj: T)

  /**
   * Update an array of objects in the database.
   *
   * @param obj the objects to be networkUpdate.
   */
  @Update
  suspend fun update(obj: List<T>)

  /**
   * Delete an object from the database
   *
   * @param obj the object to be deleted
   */
  @Delete
  suspend fun delete(obj: T)

  /**
   * Delete an array of objects in the database.
   *
   * @param obj the objects to be deleted.
   */
  @Delete
  suspend fun delete(obj: List<T>)
}