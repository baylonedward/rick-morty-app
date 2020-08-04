package com.android.component.rickmorty_api_component.data.repository

import com.android.component.rickmorty_api_component.data.local.CharacterDao
import com.android.component.rickmorty_api_component.data.remote.CharacterRemoteDataSource
import com.android.component.rickmorty_api_component.utils.performGetOperation
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */
@ExperimentalCoroutinesApi
class CharacterRepository(
  private val remoteDataSource: CharacterRemoteDataSource,
  private val localDataSource: CharacterDao
) {

  fun getCharacter(id: Int) = performGetOperation(
    databaseQuery = { localDataSource.getCharacter(id) },
    networkCall = { remoteDataSource.getCharacter(id) },
    saveCallResult = { localDataSource.insert(it) }
  )

  fun getCharacters() = performGetOperation(
    databaseQuery = { localDataSource.getCharacters() },
    networkCall = { remoteDataSource.getCharacters() },
    saveCallResult = { localDataSource.insert(it.results) }
  )
}