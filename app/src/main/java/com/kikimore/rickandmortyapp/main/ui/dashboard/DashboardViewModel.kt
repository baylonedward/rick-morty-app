package com.kikimore.rickandmortyapp.main.ui.dashboard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.component.rickmorty_api_component.RickAndMortyApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DashboardViewModel(application: Application) : ViewModel() {

  private val api = RickAndMortyApi.getInstance(application)
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