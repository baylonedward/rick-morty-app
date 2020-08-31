package com.kikimore.rickandmortyapp.main.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.android.component.rickmorty_api_component.RickAndMortyApi
import com.android.component.rickmorty_api_component.data.entities.character.Character
import com.android.component.rickmorty_api_component.utils.Resource
import com.bumptech.glide.Glide
import com.kikimore.rickandmortyapp.R
import com.kikimore.rickandmortyapp.main.ui.episode.EpisodeListAdapter
import com.kikimore.rickandmortyapp.utils.fetchViewModel
import kotlinx.android.synthetic.main.fragment_character.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class CharacterFragment : Fragment() {
  private val api by lazy { RickAndMortyApi(requireActivity().application) }
  private val viewModel by lazy { requireActivity().fetchViewModel { CharacterViewModel(api) } }
  private val episodeListAdapter by lazy {
    EpisodeListAdapter(
      viewModel
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    sharedElementEnterTransition =
      TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    // trigger episodes fetch
    viewModel.getCharacterAndEpisodes()
    setObserver()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_character, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setTransitions(view)
    setEpisodeListAdapter(view.episodeListView)
  }

  override fun onDestroy() {
    super.onDestroy()
    viewModel.unloadEpisodes()
  }

  private fun setObserver() {
    viewModel.characterAndEpisodeState().onEach {
      if (it == null) return@onEach
      when (it.status) {
        Resource.Status.SUCCESS -> {
          it.data?.character?.also { character -> setCharacter(character) }
          episodeListAdapter.notifyDataSetChanged()
        }
        Resource.Status.LOADING -> {
        }
        Resource.Status.ERROR -> {
          Toast.makeText(this.context, "${it.message}", Toast.LENGTH_SHORT).show()
        }
      }
    }.launchIn(lifecycleScope)
  }

  private fun setCharacter(character: Character) {
    view?.apply {
      // image url
      Glide.with(this)
        .load(character.image)
        .centerInside()
        .into(characterImageView)
      // name
      nameTextView.text = character.name
      // status
      val statusDrawable = if (character.status.contains(CharacterViewModel.DEAD, true))
        ContextCompat.getDrawable(context, R.drawable.ic_dot_red)
      else
        ContextCompat.getDrawable(context, R.drawable.ic_dot_green)
      statusImageView.setImageDrawable(statusDrawable)
      // specie and gender
      val specieGender = "${character.species} - ${character.gender}"
      specieGenderTextView.text = specieGender
      // origin
      originTextView.text = character.status
      //location
      locationTextView.text = character.created
    }
  }

  private fun setTransitions(view: View) {
    val characterId = viewModel.getSelectedCharacter().value?.characterId
    val nameSharedElement = "${CharacterViewModel.CHARACTER_NAME_LABEL}${characterId}"
    val imageSharedElement = "${CharacterViewModel.CHARACTER_IMAGE_LABEL}${characterId}"
    val containerSharedElement = "${CharacterViewModel.CHARACTER_CONTAINER_LABEL}${characterId}"
    ViewCompat.setTransitionName(view.nameTextView, nameSharedElement)
    ViewCompat.setTransitionName(view.characterImageView, imageSharedElement)
    ViewCompat.setTransitionName(view.containerView, containerSharedElement)
  }

  private fun setEpisodeListAdapter(recyclerView: RecyclerView) {
    recyclerView.apply {
      layoutManager = LinearLayoutManager(this@CharacterFragment.requireContext())
      adapter = this@CharacterFragment.episodeListAdapter
    }
  }
}