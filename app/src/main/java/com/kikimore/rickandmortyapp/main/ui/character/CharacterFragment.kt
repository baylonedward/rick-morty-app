package com.kikimore.rickandmortyapp.main.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.kikimore.rickandmortyapp.R
import kotlinx.android.synthetic.main.fragment_character.view.*

class CharacterFragment : Fragment() {

  private val args: CharacterListFragmentArgs by navArgs()

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
    val view = inflater.inflate(R.layout.fragment_character, container, false)
    ViewCompat.setTransitionName(view, args.name)
    view.characterImageView.setOnClickListener {
      findNavController().popBackStack()
    }
    return view
  }
}