package com.kikimore.rickandmortyapp.main.ui.character

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kikimore.rickandmortyapp.R
import kotlinx.android.synthetic.main.character_list_item_layout.view.*

/**
 * Created by: ebaylon.
 * Created on: 08/08/2020.
 */
class CharacterListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  fun onBind(
    id: Int,
    name: String,
    status: String,
    location: String,
    episode: String,
    imageUrl: String?
  ) {
    itemView.apply {
      nameTextView.text = name
      statusTextView.text = status
      locationTextView.text = location
      episodeTextView.text = episode
      // status
      statusTextView.apply {
        val drawable = if (status.contains(DEAD, true)) {
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
        .fitCenter()
        .into(characterImageView)
      // transition name
      ViewCompat.setTransitionName(characterImageView, name)
      // on click
      characterCardView.setOnClickListener {
        if (id < 0) return@setOnClickListener
        val extras = FragmentNavigatorExtras(characterImageView to name)
        val action =
          CharacterListFragmentDirections.actionNavigationCharactersToNavigationCharacter(name)
        findNavController().navigate(action, extras)
      }
    }
  }

  companion object {
    private const val DEAD = "dead"
  }
}