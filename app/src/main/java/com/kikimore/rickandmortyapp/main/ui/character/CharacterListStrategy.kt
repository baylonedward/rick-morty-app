package com.kikimore.rickandmortyapp.main.ui.character

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator

/**
 * Created by: ebaylon.
 * Created on: 05/09/2020.
 */
interface CharacterListStrategy {
  /**
   * Character List methods: Start
   */

  fun characterCount(): Int
  fun getId(position: Int): Int
  fun getName(position: Int): String
  fun getStatus(position: Int): String
  fun getLocation(position: Int): String
  fun getFirstEpisode(position: Int): String
  fun getImageUrl(position: Int): String?
  fun onSelectCharacter(position: Int): (view: View, action: NavDirections, extras: FragmentNavigator.Extras) -> Unit
  fun onLoadMoreCharacters(position: Int)
  fun setCharacterAnimation(view: View, position: Int, lastPosition: Int? = null): Int
}