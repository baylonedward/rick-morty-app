package com.kikimore.rickandmortyapp.main.ui.character

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kikimore.rickandmortyapp.R
import kotlinx.android.synthetic.main.character_list_item_layout.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by: ebaylon.
 * Created on: 08/08/2020.
 */
@ExperimentalCoroutinesApi
class CharacterListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  fun onBind(
    id: Int,
    name: String,
    status: String,
    location: String,
    episode: String,
    imageUrl: String?,
    onSelect: (view: View, actions: NavDirections, extras: FragmentNavigator.Extras) -> Unit
  ) {
    itemView.apply {
      nameTextView.text = name
      statusTextView.text = status
      locationTextView.text = location
      episodeTextView.text = episode
      // status
      statusTextView.apply {
        val drawable = if (status.contains(CharacterViewModel.DEAD, true)) {
          ContextCompat.getDrawable(itemView.context, R.drawable.ic_dot_red)
        } else {
          ContextCompat.getDrawable(itemView.context, R.drawable.ic_dot_green)
        }
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        text = status
      }
      // character image
      Glide.with(itemView)
        .load(imageUrl)
        .centerInside()
        .into(characterImageView)
      // transition name
      val nameSharedElement = "${CharacterViewModel.CHARACTER_NAME_LABEL}$id"
      val imageShareElement = "${CharacterViewModel.CHARACTER_IMAGE_LABEL}$id"
      val containerSharedElement = "${CharacterViewModel.CHARACTER_CONTAINER_LABEL}$id"
      ViewCompat.setTransitionName(nameTextView, nameSharedElement)
      ViewCompat.setTransitionName(characterImageView, imageShareElement)
      ViewCompat.setTransitionName(characterCardView, containerSharedElement)
      // navigate to character fragment
      val extras: FragmentNavigator.Extras = FragmentNavigatorExtras(
        nameTextView to nameSharedElement,
        characterImageView to imageShareElement,
        characterCardView to containerSharedElement
      )
      val action =
        CharacterListFragmentDirections.actionNavigationCharactersToNavigationCharacter()
      // on click
      characterCardView.setOnClickListener {
        if (id < 0) return@setOnClickListener
        // updated selected
        onSelect(itemView, action, extras)
      }
    }
  }
}