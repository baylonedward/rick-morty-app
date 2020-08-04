package com.android.component.rickmorty_api_component.data.local

import androidx.room.Dao
import androidx.room.Query
import com.android.component.rickmorty_api_component.data.entities.Character
import kotlinx.coroutines.flow.Flow

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */
@Dao
interface CharacterDao : BaseDao<Character> {

  @Query("SELECT * FROM characters")
  fun getCharacters(): Flow<List<Character>>

  @Query("SELECT * FROM characters WHERE id = :id")
  fun getCharacter(id: Int): Flow<Character>

  @Query("DELETE FROM characters")
  suspend fun deleteCharacters()

  @Query("DELETE FROM characters WHERE id = :id")
  suspend fun deleteCharacter(id: Int)
}