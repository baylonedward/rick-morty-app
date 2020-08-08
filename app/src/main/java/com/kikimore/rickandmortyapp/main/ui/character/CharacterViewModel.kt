package com.kikimore.rickandmortyapp.main.ui.character

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.component.rickmorty_api_component.RickAndMortyApi
import com.android.component.rickmorty_api_component.data.entities.Character
import com.android.component.rickmorty_api_component.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class CharacterViewModel(application: Application) : AndroidViewModel(application) {

  private val api = RickAndMortyApi(getApplication())
  private val characters = MutableStateFlow<List<Character>?>(null)
  private val _state = MutableStateFlow<Resource<List<Character>>?>(null)

  init {
    getCharacters()
  }

  private fun getCharacters() {
    api.characterRepository().getCharacters()
      .distinctUntilChanged()
      .catch { }
      .onEach {
        _state.value = it
        characters.value = it.data
      }.launchIn(viewModelScope)
  }

  private fun getCharacter(position: Int): Character? {
    return characters.value?.get(position)
  }

  fun state() = _state

  fun characterCount() = characters.value?.count() ?: 0

  fun getName(position: Int): String {
    return characters.value?.get(position)?.name ?: NOT_APPLICABLE
  }

  fun getStatus(position: Int): String {
    val character = getCharacter(position)
    return "${character?.status} - ${character?.species}"
  }

  fun getLocation(position: Int): String {
    return getCharacter(position)?.gender ?: NOT_APPLICABLE
  }

  fun getEpisode(position: Int): String {
    return getCharacter(position)?.created ?: NOT_APPLICABLE
  }

  fun getImageUrl(position: Int): String? {
    return getCharacter(position)?.image
  }

  companion object {
    private const val NOT_APPLICABLE = "N/A"
  }
}