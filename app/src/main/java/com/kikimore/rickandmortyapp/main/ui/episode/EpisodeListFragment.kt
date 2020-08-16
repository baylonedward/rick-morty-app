package com.kikimore.rickandmortyapp.main.ui.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.kikimore.rickandmortyapp.R
import com.kikimore.rickandmortyapp.utils.fetchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class EpisodeListFragment : Fragment() {
  private val dashboardViewModel by lazy { fetchViewModel { EpisodeViewModel(requireActivity().application) } }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val root = inflater.inflate(R.layout.fragment_episodes, container, false)
    val textView: TextView = root.findViewById(R.id.text_dashboard)
    dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
      textView.text = it
    })
    return root
  }
}