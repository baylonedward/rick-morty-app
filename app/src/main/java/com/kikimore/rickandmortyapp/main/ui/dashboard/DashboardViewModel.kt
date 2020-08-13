package com.kikimore.rickandmortyapp.main.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.component.rickmorty_api_component.RickAndMortyApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DashboardViewModel constructor(application: Application) :
  AndroidViewModel(application) {

  private val api = RickAndMortyApi(getApplication())
  private val _text = MutableLiveData<String>().apply {
    value = "This is dashboard Fragment"
  }
  val text: LiveData<String> = _text

  init {
    getData()
  }

  private fun getData() {
  }
}