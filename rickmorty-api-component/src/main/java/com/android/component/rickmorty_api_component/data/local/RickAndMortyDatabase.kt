package com.android.component.rickmorty_api_component.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.component.rickmorty_api_component.data.entities.character.Character
import com.android.component.rickmorty_api_component.data.entities.episode.Episode
import com.android.component.rickmorty_api_component.data.entities.pivot.CharacterEpisodePivot

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */
@Database(
  entities = [Character::class, Episode::class, CharacterEpisodePivot::class],
  version = 3,
  exportSchema = false
)
abstract class RickAndMortyDatabase : RoomDatabase() {

  abstract fun characterDao(): CharacterDao
  abstract fun episodeDao(): EpisodeDao
  abstract fun characterEpisodePivotDao(): CharacterEpisodePivotDao

  companion object {
    @Volatile
    private var instance: RickAndMortyDatabase? = null

    fun getDatabase(context: Context): RickAndMortyDatabase =
      instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

    private fun buildDatabase(appContext: Context) =
      Room.databaseBuilder(appContext, RickAndMortyDatabase::class.java, "characters")
        .fallbackToDestructiveMigration()
        .build()
  }
}