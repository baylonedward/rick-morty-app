package com.kikimore.rickandmortyapp.main.ui.episode

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator

/**
 * Created by: ebaylon.
 * Created on: 15/08/2020.
 */
interface EpisodeListStrategy {

  fun getEpisodeSummary(position: Int): String

  fun getEpisodeAirDate(position: Int): String

  fun getEpisodeCount(): Int

  fun onEpisodeSelect(position: Int): (view: View, action: NavDirections, extras: FragmentNavigator.Extras) -> Unit
}