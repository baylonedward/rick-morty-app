package com.android.component.rickmorty_api_component.data.entities

/**
 * Created by: ebaylon.
 * Created on: 14/08/2020.
 */
data class EntityList<T>(
  val info: Info,
  val results: List<T>
)