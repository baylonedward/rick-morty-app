package com.android.component.rickmorty_api_component

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by: ebaylon.
 * Created on: 01/08/2020.
 */

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ApiTest {
  private lateinit var testSetup: ApiTestSetup

  @Before
  fun setup() {
    testSetup = ApiTestSetup()
  }

  @Test
  fun getAllCharacters() {
    runBlocking {
      val request = testSetup.characterService().getAllCharacters()
      println(request.body())
      assertNotEquals(0, request.body()?.results?.size)
      assertNotNull(request.body()?.results?.get(0)?.characterId)
    }
  }

  @Test
  fun getCharactersByPage() {
    runBlocking {
      val pageNumber = 2
      val request = testSetup.characterService().getCharactersByPage(pageNumber)
      assertNotNull(request.body()?.info?.prev)
      assertNotEquals(0, request.body()?.results?.size)
    }
  }

  @Test
  fun getCharacter() {
    val characterId = 1
    runBlocking {
      val request = testSetup.characterService().getCharacter(characterId)
      assertEquals(characterId, request.body()?.characterId)
    }
  }

  @Test
  fun getAllEpisodes() {
    // All Episodes
    runBlocking {
      val request = testSetup.episodeService().getAllEpisodes()
      assertNotEquals(0, request.body()?.results?.size)
      assertNotNull(request.body())
    }
  }

  @Test
  fun getSelectedEpisodes() {
    // Selected Episodes
    runBlocking {
      val episodes = arrayOf(24, 28)
      val request = testSetup.episodeService()
        .getEpisodes(episodes.joinToString(separator = ",", prefix = "[", postfix = "]"))
      assertEquals(episodes.size, request.body()?.size)
      assertNotNull(request.body())
    }
  }

  @Test
  fun getEpisodesByPage() {
    runBlocking {
      val pageNumber = 2
      val request = testSetup.episodeService().getEpisodeByPage(pageNumber)
      assertNotNull(request.body()?.info?.prev)
      assertNotEquals(0, request.body()?.results?.size)
    }
  }

  @Test
  fun getEpisode() {
    // Single
    runBlocking {
      val id = 1
      val request = testSetup.episodeService().getEpisode(id)
      assertEquals(id, request.body()?.episodeId)
    }
  }
}