package com.kikimore.rickandmortyapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Generic factory method for view model to avoid one factory per view model
 */
class ViewModelFactory<T>(val creator: () -> T) : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST")
    return creator() as T
  }
}