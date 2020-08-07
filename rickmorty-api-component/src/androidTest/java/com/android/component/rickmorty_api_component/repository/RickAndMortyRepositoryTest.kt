package com.android.component.rickmorty_api_component.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.component.rickmorty_api_component.AndroidTestSetup
import com.android.component.rickmorty_api_component.utils.Resource
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
 * Created on: 08/08/2020.
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RickAndMortyRepositoryTest {

  private lateinit var testSetup: AndroidTestSetup

  @Before
  fun start() {
    testSetup = AndroidTestSetup()
  }

  @After
  @Throws(IOException::class)
  fun end() {
    testSetup.closeDb()
  }

  @Test
  fun getCharactersTest() {
    runBlocking {
      // Multiple
      launch(Dispatchers.IO) {
        testSetup.characterRepository().getCharacters().collect {
          when (it.status) {
            Resource.Status.LOADING -> {
              Assert.assertNull(it.data)
            }
            Resource.Status.SUCCESS -> {
              Assert.assertNotNull(it.data)
              cancel()
            }
            Resource.Status.ERROR -> {
              Assert.assertNotNull(it.message)
              cancel()
            }
          }
        }
      }
      // Single
      launch(Dispatchers.IO) {
        val id = 1
        testSetup.characterRepository().getCharacter(id).collect {
          when (it.status) {
            Resource.Status.LOADING -> {
              Assert.assertNull(it.data)
            }
            Resource.Status.SUCCESS -> {
              Assert.assertEquals(1, it.data?.id)
              cancel()
            }
            Resource.Status.ERROR -> {
              Assert.assertNotNull(it.message)
              cancel()
            }
          }
        }
      }
    }
  }
}