package com.android.component.rickmorty_api_component.data.repository

import com.android.component.rickmorty_api_component.data.local.EpisodeDao
import com.android.component.rickmorty_api_component.data.remote.EpisodeRemoteDataSource
import com.android.component.rickmorty_api_component.utils.performGetOperation
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by: ebaylon.
 * Created on: 14/08/2020.
 */
@ExperimentalCoroutinesApi
class EpisodeRepository(
  private val episodeRemoteDataSource: EpisodeRemoteDataSource,
  private val episodeLocalDataSource: EpisodeDao
) {

  fun getEpisodes() = performGetOperation(
    databaseQuery = { episodeLocalDataSource.getEpisodes() },
    networkCall = { episodeRemoteDataSource.getEpisodes() },
    saveCallResult = { episodeLocalDataSource.insert(it.results) }
  )

  fun getEpisodes(ids: Array<Int>) = performGetOperation(
    databaseQuery = { episodeLocalDataSource.getEpisodes() },
    networkCall = { episodeRemoteDataSource.getEpisodes(ids) },
    saveCallResult = { episodeLocalDataSource.insert(it) }
  )

  fun getEpisodes(pageNumber: Int) = performGetOperation(
    databaseQuery = { episodeLocalDataSource.getEpisodes() },
    networkCall = { episodeRemoteDataSource.getEpisodes(pageNumber) },
    saveCallResult = { episodeLocalDataSource.insert(it.results) }
  )
}