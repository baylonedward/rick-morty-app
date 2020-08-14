package com.android.component.rickmorty_api_component.data.entities.episode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by: ebaylon.
 * Created on: 14/08/2020.
 */
@Entity(tableName = "episodes")
data class Episode(
  @SerializedName("id")
  @PrimaryKey
  @ColumnInfo(index = true)
  val episodeId: Int,
  val name: String,
  @SerializedName("air_date") val airDate: String,
  val episode: String,
  val url: String,
  val created: String
)