package com.kikimore.rickandmortyapp.main.ui.character

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.android.component.rickmorty_api_component.RickAndMortyApi
import com.android.component.rickmorty_api_component.data.entities.character.Character
import com.android.component.rickmorty_api_component.data.entities.character.CharacterEpisodes
import com.android.component.rickmorty_api_component.data.entities.episode.Episode
import com.android.component.rickmorty_api_component.utils.Resource
import com.kikimore.rickandmortyapp.main.ui.episode.EpisodeListStrategy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class CharacterViewModel(private val api: RickAndMortyApi) : ViewModel(), EpisodeListStrategy {

  private val characters = MutableStateFlow<List<Character>?>(null)
  private val _characterListState = MutableStateFlow<Resource<List<Character>>?>(null)
  private val episodes = MutableStateFlow<List<Episode>?>(null)
  private val _characterAndEpisodeState = MutableStateFlow<Resource<CharacterEpisodes>?>(null)
  private val _selectedCharacter = MutableStateFlow<Character?>(null)

  private fun getCharacter(position: Int): Character? = characters.value?.get(position)

  private fun getEpisode(position: Int) = episodes.value?.get(position)

  /**
   * Selected Character Methods
   */

  fun getSelectedCharacter() = _selectedCharacter

  fun getCharacterAndEpisodes() {
    _selectedCharacter.value?.characterId?.also { id ->
      api.characterRepository().getCharacterAndEpisodes(id)
        .distinctUntilChanged()
        .catch { }
        .onEach {
          _characterAndEpisodeState.value = it
          it.data?.episodes?.also { list ->
            episodes.value = list.sortedBy { episode -> episode.episodeId }
          }
        }.launchIn(viewModelScope)
    }
  }

  fun characterAndEpisodeState() = _characterAndEpisodeState

  /**
   * Character List methods: Start
   */
  fun getCharacters() {
    api.characterRepository().getCharacters()
      .distinctUntilChanged()
      .catch { }
      .onEach {
        _characterListState.value = it
        if (it.data != null) characters.value = it.data
      }.launchIn(viewModelScope)
  }

  fun characterListState() = _characterListState

  fun characterCount() = characters.value?.count() ?: 0

  fun getId(position: Int) = getCharacter(position)?.characterId ?: 0

  fun getName(position: Int): String {
    return getCharacter(position)?.name ?: NOT_APPLICABLE
  }

  fun getStatus(position: Int): String {
    val character = getCharacter(position)
    return "${character?.status} - ${character?.species}"
  }

  fun getLocation(position: Int): String {
    return getCharacter(position)?.gender ?: NOT_APPLICABLE
  }

  fun getFirstEpisode(position: Int): String {
    return getCharacter(position)?.created ?: NOT_APPLICABLE
  }

  fun getImageUrl(position: Int): String? {
    return getCharacter(position)?.image
  }

  fun onSelectCharacter(position: Int): (view: View, action: NavDirections, extras: FragmentNavigator.Extras) -> Unit =
    { view, action, extras ->
      val character = getCharacter(position)
      _selectedCharacter.value = character
      view.findNavController().navigate(action, extras)
    }

  fun loadMoreCharacters() {
    val pageNumber = (characterCount() / API_DEFAULT_PAGE_SIZE) + 1
    api.characterRepository().getCharacters(pageNumber).launchIn(viewModelScope)
  }

  fun endOffset() = LIST_END_OFFSET

  fun unloadEpisodes() {
    episodes.value = null
  }

  /**
   * EpisodeListStrategy Methods
   */

  override fun getEpisodeSummary(position: Int): String {
    val episode = getEpisode(position)
    return "${episode?.episode}: ${episode?.name}"
  }

  override fun getEpisodeAirDate(position: Int): String {
    return "${getEpisode(position)?.airDate}"
  }

  override fun getEpisodeCount(): Int {
    return episodes.value?.size ?: 0
  }

  override fun onEpisodeClick(position: Int): () -> Unit = {
    println("Episode Click: ${getEpisode(position)}")
  }

  companion object {
    private const val LIST_END_OFFSET = 5
    private const val API_DEFAULT_PAGE_SIZE = 20
    private const val NOT_APPLICABLE = "N/A"

    const val DEAD = "dead"
    const val CHARACTER_NAME_LABEL = "characterName"
    const val CHARACTER_IMAGE_LABEL = "characterImage"
    const val CHARACTER_CONTAINER_LABEL = "characterContainer"
  }
}