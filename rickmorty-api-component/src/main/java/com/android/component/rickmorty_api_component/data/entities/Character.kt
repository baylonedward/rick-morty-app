package com.android.component.rickmorty_api_component.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by: ebaylon.
 * Created on: 23/07/2020.
 */
@Entity(tableName = "characters")
data class Character(
  val created: String,
  val gender: String,
  @PrimaryKey
  val id: Int,
  val image: String,
  val name: String,
  val species: String,
  val status: String,
  val type: String,
  val url: String
)