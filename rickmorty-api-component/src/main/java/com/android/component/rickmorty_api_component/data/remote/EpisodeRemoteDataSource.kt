package com.android.component.rickmorty_api_component.data.remote

/**
 * Created by: ebaylon.
 * Created on: 14/08/2020.
 */
class EpisodeRemoteDataSource(private val episodeService: EpisodeService) : BaseDataResource() {

  suspend fun getEpisodes() = getResult { episodeService.getAllEpisodes() }

  suspend fun getEpisodes(ids: Array<Int>) =
    getResult { episodeService.getEpisodes(ids.joinToString(separator = ",")) }

  suspend fun getEpisodes(pageNumber: Int) =
    getResult { episodeService.getEpisodeByPage(pageNumber) }

  suspend fun getEpisode(id: Int) = getResult { episodeService.getEpisode(id) }
}