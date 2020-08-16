package com.kikimore.rickandmortyapp.main.ui.episode

/**
 * Created by: ebaylon.
 * Created on: 15/08/2020.
 */
interface EpisodeListStrategy {

  fun getEpisodeSummary(position: Int): String

  fun getEpisodeAirDate(position: Int): String

  fun getEpisodeCount(): Int

  fun onEpisodeClick(position: Int): () -> Unit
}