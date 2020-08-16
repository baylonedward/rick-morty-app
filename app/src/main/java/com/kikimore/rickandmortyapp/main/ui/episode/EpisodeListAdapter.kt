package com.kikimore.rickandmortyapp.main.ui.episode

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kikimore.rickandmortyapp.R
import kotlinx.android.synthetic.main.episode_list_item_layout.view.*

/**
 * Created by: ebaylon.
 * Created on: 15/08/2020.
 */
class EpisodeListAdapter(private val episodeListStrategy: EpisodeListStrategy) :
  RecyclerView.Adapter<EpisodeListAdapter.EpisodeListViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeListViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater.inflate(R.layout.episode_list_item_layout, parent, false)
    return EpisodeListViewHolder(
      view
    )
  }

  override fun getItemCount(): Int = episodeListStrategy.getEpisodeCount()

  override fun onBindViewHolder(holder: EpisodeListViewHolder, position: Int) {
    holder.bind(
      episodeListStrategy.getEpisodeSummary(position),
      episodeListStrategy.onEpisodeClick(position)
    )
  }

  class EpisodeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(summary: String, onClick: () -> Unit) {
      itemView.episodeDetailTextView.text = summary
      itemView.episodeCardView.setOnClickListener {
        onClick()
      }
    }
  }
}