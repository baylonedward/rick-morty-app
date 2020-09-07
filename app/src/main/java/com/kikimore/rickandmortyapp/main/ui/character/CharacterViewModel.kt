package com.kikimore.rickandmortyapp.main.ui.character

import android.view.View
import android.view.animation.AnimationUtils
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
import com.kikimore.rickandmortyapp.R
import com.kikimore.rickandmortyapp.main.ui.episode.EpisodeListStrategy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
@FlowPreview
class CharacterViewModel(private val api: RickAndMortyApi) : ViewModel(), EpisodeListStrategy,
  CharacterListStrategy {

  private val characters = MutableStateFlow<List<Character>?>(null)
  private val filteredCharacters = MutableStateFlow<List<Character>?>(null)
  private val _characterListState = MutableStateFlow<Resource<List<Character>>?>(null)
  private val episodes = MutableStateFlow<List<Episode>?>(null)
  private val _characterAndEpisodeState = MutableStateFlow<Resource<CharacterEpisodes>?>(null)
  private val _selectedCharacter = MutableStateFlow<Character?>(null)
  private val searchText = MutableStateFlow<String?>(null)
  private val searchResult = MutableStateFlow<String?>(null)

  init {
    // observe search field from the beginning
    searchObserver()
  }

  private fun getCharacter(position: Int): Character? = filteredCharacters.value?.get(position)

  private fun getEpisode(position: Int) = episodes.value?.get(position)

  private fun searchObserver() {
    searchText
      .debounce(500L)
      .onEach { word ->
        if (word == null) return@onEach
        if (word.isNotEmpty()) {
          filteredCharacters.value = characters.value?.filter { characters ->
            val name = characters.name.toLowerCase()
            val status = characters.status.toLowerCase()
            val species = characters.species.toLowerCase()
            val gender = characters.gender.toLowerCase()
            val finalWord = word.toLowerCase()
            name.contains(finalWord, true)
             || status.contains(finalWord, true)
             || species.contains(finalWord, true)
             || gender.equals(finalWord, true)
          }
        } else {
          filteredCharacters.value = characters.value
        }
        // update result
        searchResult.value = word
      }.launchIn(viewModelScope)
  }

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

  fun getCharacters() {
    api.characterRepository().getCharacters()
      .distinctUntilChanged()
      .catch { }
      .onEach {
        _characterListState.value = it
        if (it.data != null) {
          characters.value = it.data
          filteredCharacters.value = it.data
        }
      }.launchIn(viewModelScope)
  }

  fun characterListState() = _characterListState

  fun unloadEpisodes() {
    episodes.value = null
  }

  fun search(text: String) {
    searchText.value = text
  }

  fun getSearchResult() = searchResult

  /**
   * CharacterList Strategy methods
   */

  override fun characterCount() = filteredCharacters.value?.count() ?: 0

  override fun getId(position: Int) = getCharacter(position)?.characterId ?: 0

  override fun getName(position: Int): String {
    return getCharacter(position)?.name ?: NOT_APPLICABLE
  }

  override fun getStatus(position: Int): String {
    val character = getCharacter(position)
    return "${character?.status} - ${character?.species}"
  }

  override fun getLocation(position: Int): String {
    return getCharacter(position)?.gender ?: NOT_APPLICABLE
  }

  override fun getFirstEpisode(position: Int): String {
    return getCharacter(position)?.created ?: NOT_APPLICABLE
  }

  override fun getImageUrl(position: Int): String? {
    return getCharacter(position)?.image
  }

  override fun onCharacterSelect(position: Int): (view: View, action: NavDirections, extras: FragmentNavigator.Extras) -> Unit =
    { view, action, extras ->
      val character = getCharacter(position)
      _selectedCharacter.value = character
      view.findNavController()
        .popBackStack(action.actionId, true) // if fragment already exist pop it first
      view.findNavController().navigate(action, extras)
    }

  override fun onLoadMoreCharacters(position: Int) {
    // if searching locally don't load more characters
    if (characters.value != filteredCharacters.value) return
    // if list position = offset before end of list load more characters
    if (position == characterCount() - LIST_END_OFFSET) {
      val pageNumber = (characterCount() / API_DEFAULT_PAGE_SIZE) + 1
      api.characterRepository().getCharacters(pageNumber).launchIn(viewModelScope)
    }
  }

  override fun setCharacterAnimation(view: View, position: Int, lastPosition: Int?): Int {
    // set item animation
    val animation = AnimationUtils.loadAnimation(
      view.context,
      R.anim.item_animation_from_right
    )
    view.startAnimation(animation)
    return position
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

  override fun onEpisodeSelect(position: Int): (view: View, action: NavDirections, extras: FragmentNavigator.Extras) -> Unit =
    { view, action, extras ->
      println("Episode Click: ${getEpisode(position)}")
      view.findNavController()
        .popBackStack(action.actionId, true) // if fragment already exist pop it first
      view.findNavController().navigate(action, extras)
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