package com.kikimore.rickandmortyapp.main

import androidx.lifecycle.ViewModel

/**
 * Created by: ebaylon.
 * Created on: 07/08/2020.
 */
class MainViewModel : ViewModel() {
  var characterCurrentPosition: Int = 0
    get() = 0
    set(value) {
      field = value
    }
}