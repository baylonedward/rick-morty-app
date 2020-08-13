package com.kikimore.rickandmortyapp.main.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.kikimore.rickandmortyapp.R
import com.kikimore.rickandmortyapp.utils.fetchViewModel
import kotlinx.android.synthetic.main.fragment_character.*
import kotlinx.android.synthetic.main.fragment_character.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class CharacterFragment : Fragment() {

  private val args: CharacterListFragmentArgs by navArgs()
  private val viewModel by lazy {
    requireActivity().fetchViewModel {
      CharacterViewModel(
        requireActivity().application
      )
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    sharedElementEnterTransition =
      TransitionInflater.from(context).inflateTransition(android.R.transition.move)
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
    setObserver(view)
    setTransitions(view)
  }

  private fun setObserver(view: View) {
    viewModel.selectedCharacter().onEach {
      if (it == null) return@onEach
      // image url
      Glide.with(view)
        .load(it.image)
        .fitCenter()
        .into(characterImageView)
      // name
      view.nameTextView.text = it.name
      // status
      val statusDrawable = if (it.status.contains(CharacterViewModel.DEAD, true))
        ContextCompat.getDrawable(view.context, R.drawable.ic_dot_red)
      else
        ContextCompat.getDrawable(view.context, R.drawable.ic_dot_green)
      view.statusImageView.setImageDrawable(statusDrawable)
      // specie and gender
      val specieGender = "${it.species} - ${it.gender}"
      view.specieGenderTextView.text = specieGender
      // origin
      view.originTextView.text = it.status
      //location
      view.locationTextView.text = it.created
    }.launchIn(lifecycleScope)
  }

  private fun setTransitions(view: View) {
    val nameSharedElement = "${CharacterViewModel.CHARACTER_NAME_LABEL}${args.id}"
    val imageSharedElement = "${CharacterViewModel.CHARACTER_IMAGE_LABEL}${args.id}"
    val containerSharedElement = "${CharacterViewModel.CHARACTER_CONTAINER_LABEL}${args.id}"
    ViewCompat.setTransitionName(view.nameTextView, nameSharedElement)
    ViewCompat.setTransitionName(view.characterImageView, imageSharedElement)
    ViewCompat.setTransitionName(view.containerView, containerSharedElement)
  }
}