package com.android.component.rickmorty_api_component

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.android.component.rickmorty_api_component.data.local.RickAndMortyDatabase
import com.android.component.rickmorty_api_component.data.remote.CharacterRemoteDataSource
import com.android.component.rickmorty_api_component.data.remote.CharacterService
import com.android.component.rickmorty_api_component.data.repository.CharacterRepository
import com.android.component.rickmorty_api_component.utils.loggingInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by: ebaylon.
 * Created on: 08/08/2020.
 */
@ExperimentalCoroutinesApi
class AndroidTestSetup {
  private val context = ApplicationProvider.getApplicationContext<Context>()
  private val db = Room.inMemoryDatabaseBuilder(context, RickAndMortyDatabase::class.java).build()
  private val baseUrl = "https://rickandmortyapi.com/api/"
  private val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient.Builder().loggingInterceptor().build())
    .build()
  private val characterService = retrofit.create(CharacterService::class.java)
  private val characterRemoteDataSource = CharacterRemoteDataSource(characterService)

  fun characterDao() = db.characterDao()
  fun closeDb() = db.close()

  fun characterRepository(): CharacterRepository {
    return CharacterRepository(
      characterLocalDataSource = db.characterDao(),
      characterRemoteDataSource = characterRemoteDataSource,
      characterEpisodePivotLocalDataSource = db.characterEpisodePivotDao()
    )
  }

  @Throws(IOException::class)
  suspend fun readJsonFile(filename: String): String? {
    return withContext(Dispatchers.IO) {
      val inputStream =
        InstrumentationRegistry.getInstrumentation().context.assets.open(filename)
      val br = BufferedReader(InputStreamReader(inputStream))
      val sb = StringBuilder()
      var line: String? = br.readLine()
      while (line != null) {
        sb.append(line)
        line = br.readLine()
      }
      sb.toString()
    }
  }
}