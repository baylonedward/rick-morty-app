package com.android.component.rickmorty_api_component.data.entities.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.android.component.rickmorty_api_component.data.entities.NameUrl
import com.google.gson.annotations.SerializedName

/**
 * Created by: ebaylon.
 * Created on: 23/07/2020.
 */
@Entity(tableName = "characters")
data class Character(
  @SerializedName("id")
  @ColumnInfo(index = true)
  @PrimaryKey
  val characterId: Int,
  val name: String,
  val species: String,
  val status: String,
  val type: String,
  val gender: String,
  val image: String,
  val url: String,
  val created: String,
  @Ignore val origin: NameUrl?,
  @Ignore val location: NameUrl?,
  @Ignore
  @SerializedName("episode")
  val episodes: List<String>?
) {
  constructor(
    characterId: Int,
    name: String,
    species: String,
    status: String,
    type: String,
    gender: String,
    image: String,
    url: String,
    created: String
  ) : this(characterId, name, species, status, type, gender, image, url, created, null, null, null)
}