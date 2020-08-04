package com.android.component.rickmorty_api_component

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.component.rickmorty_api_component.data.entities.CharacterList
import com.android.component.rickmorty_api_component.data.local.CharacterDao
import com.google.gson.GsonBuilder
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

  private lateinit var testSetup: DatabaseTestSetup
  private lateinit var characterDao: CharacterDao

  @Before
  fun start() {
    testSetup = DatabaseTestSetup()
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
      val characterList =
        GsonBuilder().create().fromJson(charactersString, CharacterList::class.java)
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
      val characters = testSetup.readJsonFile("characters.json")
      val characterList = GsonBuilder().create().fromJson(characters, CharacterList::class.java)
      // Insert Single
      launch {
        val character = characterList.results[0]
        characterDao.insert(character)
        characterDao.getCharacter(character.id).collect {
          Assert.assertEquals(character.id, it.id)
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