package com.android.component.rickmorty_api_component

import android.app.Application
import android.content.Context
import com.android.component.rickmorty_api_component.data.local.RickAndMortyDatabase
import com.android.component.rickmorty_api_component.data.remote.CharacterRemoteDataSource
import com.android.component.rickmorty_api_component.data.remote.CharacterService
import com.android.component.rickmorty_api_component.data.repository.CharacterRepository
import com.android.component.rickmorty_api_component.utils.loggingInterceptor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class RickAndMortyApi(context: Context) {
  private val baseUrl = "https://rickandmortyapi.com/api/"
  private val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient.Builder().loggingInterceptor().build())
    .build()
  private val characterService = retrofit.create(CharacterService::class.java)
  private val characterRemoteDataSource = CharacterRemoteDataSource(characterService)
  private val db = RickAndMortyDatabase.getDatabase(context)

  fun characterRepository(): CharacterRepository {
    return CharacterRepository(
      localDataSource = db.characterDao(),
      remoteDataSource = characterRemoteDataSource
    )
  }

  //singleton
  companion object {
    @Volatile
    private var instance: RickAndMortyApi? = null

    fun getInstance(application: Application, baseUrl: String): RickAndMortyApi? {
      if (instance == null) {
        println("API is null, creating new instance.")
        synchronized(RickAndMortyApi::class.java) {
          instance = RickAndMortyApi(application)
        }
      }
      return instance
    }
  }
}