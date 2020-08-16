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
) {
  override fun equals(other: Any?): Boolean {
    if (other == null || other !is Episode) return false
    return (this.episodeId == other.episodeId
     && this.episode == other.episode
     && this.airDate == other.airDate
     && this.created == other.created
     && this.name == other.name
     && this.url == other.url)
  }

  override fun hashCode(): Int {
    var result = episodeId
    result = 31 * result + name.hashCode()
    result = 31 * result + airDate.hashCode()
    result = 31 * result + episode.hashCode()
    result = 31 * result + url.hashCode()
    result = 31 * result + created.hashCode()
    return result
  }
}