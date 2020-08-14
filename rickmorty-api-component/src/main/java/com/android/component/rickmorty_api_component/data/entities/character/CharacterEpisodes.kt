package com.android.component.rickmorty_api_component.data.entities.character

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.android.component.rickmorty_api_component.data.entities.episode.Episode
import com.android.component.rickmorty_api_component.data.entities.pivot.CharacterEpisodePivot

/**
 * Created by: ebaylon.
 * Created on: 14/08/2020.
 */
data class CharacterEpisodes(
  @Embedded val character: Character,
  @Relation(
    parentColumn = "characterId",
    entityColumn = "episodeId",
    associateBy = Junction(CharacterEpisodePivot::class)
  ) val episodes: List<Episode>
)