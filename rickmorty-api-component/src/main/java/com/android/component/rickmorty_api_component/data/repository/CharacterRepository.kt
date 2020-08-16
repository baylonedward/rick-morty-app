package com.android.component.rickmorty_api_component.data.repository

import com.android.component.rickmorty_api_component.data.entities.character.Character
import com.android.component.rickmorty_api_component.data.entities.character.CharacterEpisodes
import com.android.component.rickmorty_api_component.data.entities.pivot.CharacterEpisodePivot
import com.android.component.rickmorty_api_component.data.local.CharacterDao
import com.android.component.rickmorty_api_component.data.local.CharacterEpisodePivotDao
import com.android.component.rickmorty_api_component.data.local.EpisodeDao
import com.android.component.rickmorty_api_component.data.remote.CharacterRemoteDataSource
import com.android.component.rickmorty_api_component.data.remote.EpisodeRemoteDataSource
import com.android.component.rickmorty_api_component.utils.Resource
import com.android.component.rickmorty_api_component.utils.performGetOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */
@ExperimentalCoroutinesApi
class CharacterRepository(
  private val characterRemoteDataSource: CharacterRemoteDataSource,
  private val characterLocalDataSource: CharacterDao,
  private val characterEpisodePivotLocalDataSource: CharacterEpisodePivotDao,
  private val episodeRemoteDataSource: EpisodeRemoteDataSource,
  private val episodeLocalDataSource: EpisodeDao
) {

  private suspend fun saveCharacterEpisodePivotData(list: List<Character>) {
    // Aside from saving the objects, we need to save data to pivot table
    val pivotDataList = ArrayList<CharacterEpisodePivot>()
    list.forEach { character ->
      val characterId = character.characterId
      character.episodes?.forEach {
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

  fun getCharacterAndEpisodes(characterId: Int): Flow<Resource<CharacterEpisodes>> = channelFlow {
    send(Resource.loading())
    // return observable resource
    launch {
      characterLocalDataSource.getCharacterAndEpisodes(characterId).map { Resource.success(it) }
        .collect { send(it) }
    }
    // network call
    launch {
      characterLocalDataSource.getEpisodeIds(characterId)
        .collect { pivots ->
          val ids = pivots.map { it.episodeId }.toTypedArray()
          if (ids.isEmpty()) return@collect
          val request = episodeRemoteDataSource.getEpisodes(ids)
          when (request.status) {
            Resource.Status.SUCCESS -> {
              request.data?.also { episodeLocalDataSource.insert(it) }
              cancel()
            }
            Resource.Status.ERROR -> {
              send(Resource.error(request.message!!))
              cancel()
            }
            else -> {
            }
          }
        }
    }
  }.flowOn(Dispatchers.IO)
}