package com.android.component.rickmorty_api_component.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.component.rickmorty_api_component.AndroidTestSetup
import com.android.component.rickmorty_api_component.data.entities.EntityList
import com.android.component.rickmorty_api_component.data.entities.character.Character
import com.android.component.rickmorty_api_component.data.local.CharacterDao
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by: ebaylon.
 * Created on: 25/07/2020.
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RickAndMortyDatabaseTest {

  private lateinit var testSetup: AndroidTestSetup
  private lateinit var characterDao: CharacterDao

  @Before
  fun start() {
    testSetup = AndroidTestSetup()
    characterDao = testSetup.characterDao()
  }

  @After
  @Throws(IOException::class)
  fun end() {
    testSetup.closeDb()
  }

  @Test
  fun deleteCharactersTest() {
    runBlocking(Dispatchers.IO) {
      val charactersString = testSetup.readJsonFile("characters.json")
      val type = object : TypeToken<EntityList<Character>>() {}.type
      val characterList =
        GsonBuilder().create().fromJson<EntityList<Character>>(charactersString, type)
      // Delete Single
      launch {
        val character = characterList.results[0]
        characterDao.insert(characterList.results)
        characterDao.delete(character)
        characterDao.getCharacters().collect {
          Assert.assertFalse(it.contains(character))
          cancel()
        }
      }
      // Delete Multiple
      launch {
        val characters = listOf(
          characterList.results[0],
          characterList.results[1],
          characterList.results[2]
        )
        characterDao.insert(characterList.results)
        characterDao.delete(characters)
        characterDao.getCharacters().collect {
          Assert.assertFalse(it.containsAll(characters))
          cancel()
        }
      }
    }
  }

  @Test
  fun insertCharactersTest() {
    runBlocking(Dispatchers.IO) {
      val charactersString = testSetup.readJsonFile("characters.json")
      val type = object : TypeToken<EntityList<Character>>() {}.type
      val characterList =
        GsonBuilder().create().fromJson<EntityList<Character>>(charactersString, type)
      // Insert Single
      launch {
        val character = characterList.results[0]
        characterDao.insert(character)
        characterDao.getCharacter(character.characterId).collect {
          Assert.assertEquals(character.characterId, it.characterId)
          cancel()
        }
      }
      // Insert Multiple
      launch {
        characterDao.insert(characterList.results)
        characterDao.getCharacters().collect {
          Assert.assertEquals(characterList.results.count(), it.count())
          cancel()
        }
      }
    }
  }
}