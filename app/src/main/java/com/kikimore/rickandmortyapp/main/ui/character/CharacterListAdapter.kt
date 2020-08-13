package com.kikimore.rickandmortyapp.main.ui.character

import android.view.LayoutInflater
import android.view.ViewGroup
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
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater.inflate(R.layout.character_list_item_layout, parent, false)
    return CharacterListViewHolder(view)
  }

  override fun getItemCount(): Int {
    return viewModel.characterCount()
  }

  override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
    holder.onBind(
      viewModel.getId(position),
      viewModel.getName(position),
      viewModel.getStatus(position),
      viewModel.getLocation(position),
      viewModel.getEpisode(position),
      viewModel.getImageUrl(position),
      viewModel.onSelectCharacter(position)
    )
    // if position = end offset we call method to load more data.
    if (position == viewModel.characterCount() - viewModel.endOffset()) {
      viewModel.loadMoreData()
    }
  }
}