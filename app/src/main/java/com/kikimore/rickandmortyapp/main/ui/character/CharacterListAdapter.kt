package com.kikimore.rickandmortyapp.main.ui.character

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.kikimore.rickandmortyapp.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by: ebaylon.
 * Created on: 08/08/2020.
 */
@ExperimentalCoroutinesApi
class CharacterListAdapter(private val viewModel: CharacterViewModel) :
  RecyclerView.Adapter<CharacterListViewHolder>() {

  private var lastPosition = -1

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater.inflate(R.layout.character_list_item_layout, parent, false)
    return CharacterListViewHolder(view)
  }

  override fun getItemCount(): Int {
    return viewModel.characterCount()
  }

  override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
    holder.apply {
      onBind(
        viewModel.getId(position),
        viewModel.getName(position),
        viewModel.getStatus(position),
        viewModel.getLocation(position),
        viewModel.getFirstEpisode(position),
        viewModel.getImageUrl(position),
        viewModel.onSelectCharacter(position)
      )
      // animate on first appearance
      setItemAnimation(itemView, position)
    }
    // if position = end offset we call method to load more data.
    if (position == viewModel.characterCount() - viewModel.endOffset()) {
      viewModel.loadMoreCharacters()
    }
  }

  private fun setItemAnimation(view: View, position: Int) {
    // set item animation
    val animation = AnimationUtils.loadAnimation(
      view.context,
      R.anim.item_animation_fall_down
    )
    view.startAnimation(animation)
    lastPosition = position
  }
}