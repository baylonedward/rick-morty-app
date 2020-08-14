package com.android.component.rickmorty_api_component.data.entities.pivot

import androidx.room.Entity
import androidx.room.Index

/**
 * Created by: ebaylon.
 * Created on: 14/08/2020.
 */
@Entity(
  tableName = "characters_episodes_pivot",
  primaryKeys = ["characterId", "episodeId"],
  indices = [Index("characterId"), Index("episodeId")]
)
data class CharacterEpisodePivot(
  val characterId: Int,
  val episodeId: Int
)