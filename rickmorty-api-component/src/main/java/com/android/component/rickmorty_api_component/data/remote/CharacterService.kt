package com.android.component.rickmorty_api_component.data.remote

import com.android.component.rickmorty_api_component.data.entities.Character
import com.android.component.rickmorty_api_component.data.entities.CharacterList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by: ebaylon.
 * Created on: 23/07/2020.
 */
interface CharacterService {

  @GET("character")
  suspend fun getAllCharacters(): Response<CharacterList>

  @GET("character/{id}")
  suspend fun getCharacter(@Path("id") id: Int): Response<Character>
}