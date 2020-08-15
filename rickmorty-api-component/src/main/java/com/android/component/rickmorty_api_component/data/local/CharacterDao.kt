package com.android.component.rickmorty_api_component.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.android.component.rickmorty_api_component.data.entities.character.Character
import com.android.component.rickmorty_api_component.data.entities.character.CharacterEpisodes
import com.android.component.rickmorty_api_component.data.entities.pivot.CharacterEpisodePivot
import kotlinx.coroutines.flow.Flow

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */
@Dao
interface CharacterDao : BaseDao<Character> {

  @Query("SELECT * FROM characters")
  fun getCharacters(): Flow<List<Character>>

  @Query("SELECT * FROM characters WHERE characterId = :id")
  fun getCharacter(id: Int): Flow<Character>

  @Query("DELETE FROM characters")
  suspend fun deleteCharacters()

  @Query("DELETE FROM characters WHERE characterId = :id")
  suspend fun deleteCharacter(id: Int)

  @Transaction
  @Query("SELECT * FROM characters WHERE characterId = :id LIMIT 1")
  fun getCharacterAndEpisodes(id: Int): Flow<CharacterEpisodes>

  @Query("SELECT * FROM characters_episodes_pivot WHERE characterId = :id")
  fun getEpisodeIds(id: Int): Flow<List<CharacterEpisodePivot>>
}