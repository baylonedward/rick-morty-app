package com.android.component.rickmorty_api_component

import com.android.component.rickmorty_api_component.data.remote.CharacterService
import com.android.component.rickmorty_api_component.utils.loggingInterceptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */
@ExperimentalCoroutinesApi
class ApiTestSetup {
  private val baseUrl = "https://rickandmortyapi.com/api/"
  private val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient.Builder().loggingInterceptor().build())
    .build()

  fun characterService(): CharacterService = retrofit.create(CharacterService::class.java)
}