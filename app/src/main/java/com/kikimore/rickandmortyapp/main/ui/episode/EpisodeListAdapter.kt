package com.kikimore.rickandmortyapp.main.ui.episode

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.kikimore.rickandmortyapp.R
import com.kikimore.rickandmortyapp.main.ui.character.CharacterFragmentDirections
import kotlinx.android.synthetic.main.episode_list_item_layout.view.*

/**
 * Created by: ebaylon.
 * Created on: 15/08/2020.
 */
class EpisodeListAdapter(private val episodeListStrategy: EpisodeListStrategy) :
  RecyclerView.Adapter<EpisodeListAdapter.EpisodeListViewHolder>() {

  private var lastPosition = -1

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeListViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater.inflate(R.layout.episode_list_item_layout, parent, false)
    return EpisodeListViewHolder(
      view
    )
  }

  override fun getItemCount(): Int = episodeListStrategy.getEpisodeCount()

  override fun onBindViewHolder(holder: EpisodeListViewHolder, position: Int) {
    holder.apply {
      bind(
        episodeListStrategy.getEpisodeSummary(position),
        episodeListStrategy.getEpisodeAirDate(position),
        episodeListStrategy.onEpisodeSelect(position)
      )
      // animate on first appearance
      if (position > lastPosition) {
        itemView.apply {
          animation = getItemAnimation(itemView.context)
          startAnimation(animation)
        }
        lastPosition = position
      }
    }
  }

  private fun getItemAnimation(context: Context): Animation {
    // set item animation
    return AnimationUtils.loadAnimation(
      context,
      R.anim.item_animation_from_bottom
    )
  }

  class EpisodeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
      summary: String,
      airDate: String,
      onClick: (view: View, action: NavDirections, extras: FragmentNavigator.Extras) -> Unit
    ) {
      itemView.episodeDetailTextView.text = summary
      itemView.episodeAirDateTextView.text = airDate
      itemView.episodeCardView.setOnClickListener {
        // navigate to character fragment
        val extras: FragmentNavigator.Extras = FragmentNavigatorExtras()
        val action =
          CharacterFragmentDirections.actionNavigationCharacterEpisodesToNavigationEpisode()
        onClick(itemView, action, extras)
      }
    }
  }
}