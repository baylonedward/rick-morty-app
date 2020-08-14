package com.android.component.rickmorty_api_component.data.remote

import com.android.component.rickmorty_api_component.data.entities.EntityList
import com.android.component.rickmorty_api_component.data.entities.episode.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by: ebaylon.
 * Created on: 14/08/2020.
 */
interface EpisodeService {

  @GET("episode")
  suspend fun getAllEpisodes(): Response<EntityList<Episode>>

  @GET("episode/{ids}")
  suspend fun getEpisodes(@Path("ids") ids: String): Response<List<Episode>>

  @GET("episode/{id}")
  suspend fun getEpisode(@Path("id") id: Int): Response<Episode>

  @GET("episode")
  suspend fun getEpisodeByPage(@Query("page") pageNumber: Int): Response<EntityList<Episode>>

}