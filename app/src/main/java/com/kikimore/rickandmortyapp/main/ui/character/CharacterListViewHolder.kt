package com.kikimore.rickandmortyapp.main.ui.character

import android.view.View
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
          itemView.context.getDrawable(R.drawable.ic_dot_red)
        } else {
          itemView.context.getDrawable(R.drawable.ic_dot_green)
        }
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        text = status
      }
      // character image
      Glide.with(itemView)
        .load(imageUrl)
        .fitCenter()
        .into(characterImageView)
    }
  }

  companion object {
    private const val DEAD = "dead"
  }
}