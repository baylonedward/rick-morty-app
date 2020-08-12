package com.kikimore.rickandmortyapp.main.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.component.rickmorty_api_component.utils.Resource
import com.kikimore.rickandmortyapp.R
import com.kikimore.rickandmortyapp.utils.fetchViewModel
import kotlinx.android.synthetic.main.fragment_characters.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class CharacterListFragment : Fragment() {

  private val viewModel by lazy { fetchViewModel { CharacterViewModel(requireActivity().application) } }
  private val listAdapter by lazy { CharacterListAdapter(viewModel) }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val root = inflater.inflate(R.layout.fragment_characters, container, false)
    setObservers()
    setListAdapter(root.characterListView)
    return root
  }

  private fun setObservers() {
    //

    //
    viewModel.state().onEach {
      if (it == null) return@onEach
      when (it.status) {
        Resource.Status.SUCCESS -> {
          listAdapter.notifyDataSetChanged()
        }
        Resource.Status.LOADING -> {
        }
        Resource.Status.ERROR -> {
          Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()
        }
      }
    }.launchIn(lifecycleScope)
  }

  private fun setListAdapter(recyclerView: RecyclerView) {
    recyclerView.apply {
      layoutManager = LinearLayoutManager(this@CharacterListFragment.requireContext())
      adapter = this@CharacterListFragment.listAdapter
      postponeEnterTransition()
      viewTreeObserver.addOnPreDrawListener {
        startPostponedEnterTransition()
        true
      }
    }
  }
}