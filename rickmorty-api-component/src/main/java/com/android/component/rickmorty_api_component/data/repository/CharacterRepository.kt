package com.android.component.rickmorty_api_component.data.repository

import com.android.component.rickmorty_api_component.data.entities.character.Character
import com.android.component.rickmorty_api_component.data.entities.pivot.CharacterEpisodePivot
import com.android.component.rickmorty_api_component.data.local.CharacterDao
import com.android.component.rickmorty_api_component.data.local.CharacterEpisodePivotDao
import com.android.component.rickmorty_api_component.data.remote.CharacterRemoteDataSource
import com.android.component.rickmorty_api_component.utils.performGetOperation
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */
@ExperimentalCoroutinesApi
class CharacterRepository(
  private val characterRemoteDataSource: CharacterRemoteDataSource,
  private val characterLocalDataSource: CharacterDao,
  private val characterEpisodePivotLocalDataSource: CharacterEpisodePivotDao
) {

  private suspend fun saveCharacterEpisodePivotData(list: List<Character>) {
    // Aside from saving the objects, we need to save data to pivot table
    val pivotDataList = ArrayList<CharacterEpisodePivot>()
    list.forEach { character ->
      val characterId = character.characterId
      character.episodes?.map {
        val episodeId = it.split("/").last().toInt()
        val pivotData = CharacterEpisodePivot(characterId, episodeId)
        pivotDataList.add(pivotData)
      }
    }
    characterEpisodePivotLocalDataSource.insert(pivotDataList)
  }

  fun getCharacter(id: Int) = performGetOperation(
    databaseQuery = { characterLocalDataSource.getCharacter(id) },
    networkCall = { characterRemoteDataSource.getCharacter(id) },
    saveCallResult = { characterLocalDataSource.insert(it) },
    networkCallBack = { saveCharacterEpisodePivotData(listOf(it)) }
  )

  fun getCharacters() = performGetOperation(
    databaseQuery = { characterLocalDataSource.getCharacters() },
    networkCall = { characterRemoteDataSource.getCharacters() },
    saveCallResult = { characterLocalDataSource.insert(it.results) },
    networkCallBack = { saveCharacterEpisodePivotData(it.results) }
  )

  fun getCharacters(pageNumber: Int) = performGetOperation(
    databaseQuery = { characterLocalDataSource.getCharacters() },
    networkCall = { characterRemoteDataSource.getCharacters(pageNumber) },
    saveCallResult = { characterLocalDataSource.insert(it.results) },
    networkCallBack = { saveCharacterEpisodePivotData(it.results) }
  )
}