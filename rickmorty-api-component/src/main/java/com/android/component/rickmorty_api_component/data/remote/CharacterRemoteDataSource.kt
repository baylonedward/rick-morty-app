package com.android.component.rickmorty_api_component.data.remote

/**
 * Created by: ebaylon.
 * Created on: 23/07/2020.
 */
class CharacterRemoteDataSource(private val characterService: CharacterService) :
  BaseDataResource() {

  suspend fun getCharacters() = getResult { characterService.getAllCharacters() }
  suspend fun getCharacter(id: Int) = getResult { characterService.getCharacter(id) }
}