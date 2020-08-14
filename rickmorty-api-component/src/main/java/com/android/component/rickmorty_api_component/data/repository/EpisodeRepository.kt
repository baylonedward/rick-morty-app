package com.android.component.rickmorty_api_component.data.repository

import com.android.component.rickmorty_api_component.data.entities.episode.Episode
import com.android.component.rickmorty_api_component.data.remote.EpisodeRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by: ebaylon.
 * Created on: 14/08/2020.
 */
@ExperimentalCoroutinesApi
class EpisodeRepository(
  private val remoteDataSource: EpisodeRemoteDataSource,
  private val localDataSource: Episode
) {
}