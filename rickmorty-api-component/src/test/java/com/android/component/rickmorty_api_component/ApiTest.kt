package com.android.component.rickmorty_api_component

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
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
  fun getCharacters() {
    runBlocking {
      val request = testSetup.characterService().getAllCharacters()
      println(request.body())
      assertNotNull(request.body())
      assertNotNull(request.body()?.results?.get(0)?.id)
    }
  }

  @Test
  fun getCharacter() {
    val characterId = 1
    runBlocking {
      val request = testSetup.characterService().getCharacter(characterId)
      assertNotNull(request.body())
    }
  }
}