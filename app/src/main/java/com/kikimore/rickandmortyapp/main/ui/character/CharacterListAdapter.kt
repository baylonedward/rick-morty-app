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
class CharacterListAdapter(private val characterListStrategy: CharacterListStrategy) :
  RecyclerView.Adapter<CharacterListViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater.inflate(R.layout.character_list_item_layout, parent, false)
    return CharacterListViewHolder(view)
  }

  override fun getItemCount(): Int = characterListStrategy.characterCount()

  override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
    holder.apply {
      onBind(
        characterListStrategy.getId(position),
        characterListStrategy.getName(position),
        characterListStrategy.getStatus(position),
        characterListStrategy.getLocation(position),
        characterListStrategy.getFirstEpisode(position),
        characterListStrategy.getImageUrl(position),
        characterListStrategy.onSelectCharacter(position)
      )
      // animate on first appearance
      characterListStrategy.setCharacterAnimation(itemView, position)
    }
    // if position = end offset we call method to load more data.
    characterListStrategy.onLoadMoreCharacters(position)
  }
}