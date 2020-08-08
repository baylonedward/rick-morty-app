package com.kikimore.rickandmortyapp.main.ui.dashboard

import android.app.Application
import androidx.lifecycle.*
import com.android.component.rickmorty_api_component.RickAndMortyApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
    api.characterRepository().getCharacters().distinctUntilChanged().onEach {
      println("Data: $it")
    }.launchIn(viewModelScope)
  }
}