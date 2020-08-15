package com.android.component.rickmorty_api_component.data.local

import androidx.room.Dao
import androidx.room.Query
import com.android.component.rickmorty_api_component.data.entities.episode.Episode
import kotlinx.coroutines.flow.Flow

/**
 * Created by: ebaylon.
 * Created on: 14/08/2020.
 */

@Dao
interface EpisodeDao : BaseDao<Episode> {

  @Query("SELECT * FROM episodes")
  fun getEpisodes(): Flow<List<Episode>>

  @Query("SELECT * FROM episodes WHERE episodeId IN (:ids)")
  fun getEpisodes(ids: Array<Int>): Flow<List<Episode>>

  @Query("SELECT * FROM episodes WHERE episodeId = :id LIMIT 1")
  fun getEpisode(id: Int): Flow<Episode>
}