package com.android.component.rickmorty_api_component

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.android.component.rickmorty_api_component.data.local.RickAndMortyDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by: ebaylon.
 * Created on: 01/08/2020.
 */
@ExperimentalCoroutinesApi
class DatabaseTestSetup {
  private val context = ApplicationProvider.getApplicationContext<Context>()
  private val db = Room.inMemoryDatabaseBuilder(context, RickAndMortyDatabase::class.java).build()

  fun characterDao() = db.characterDao()
  fun closeDb() = db.close()

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