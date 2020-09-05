package com.kikimore.rickandmortyapp.main.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.component.rickmorty_api_component.RickAndMortyApi
import com.android.component.rickmorty_api_component.utils.Resource
import com.kikimore.rickandmortyapp.R
import com.kikimore.rickandmortyapp.utils.fetchViewModel
import kotlinx.android.synthetic.main.fragment_characters.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class CharacterListFragment : Fragment() {
  private val api by lazy { RickAndMortyApi(requireActivity().application) }
  private val viewModel by lazy {
    requireActivity().fetchViewModel { CharacterViewModel(api) }
  }
  private val listAdapter by lazy { CharacterListAdapter(viewModel) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.getCharacters()
    setObservers()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setListAdapter(view.characterListView)
    setSearchView(view.characterSearchView)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_characters, container, false)
  }

  private fun setObservers() {
    // character list state
    viewModel.characterListState().onEach {
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
    // search result
    viewModel.getSearchResult().onEach {
      listAdapter.notifyDataSetChanged()
    }.launchIn(lifecycleScope)
  }

  private fun setListAdapter(recyclerView: RecyclerView) {
    recyclerView.apply {
      layoutManager = LinearLayoutManager(this@CharacterListFragment.requireContext())
      adapter = this@CharacterListFragment.listAdapter
      // set transition
      postponeEnterTransition()
      viewTreeObserver.addOnPreDrawListener {
        startPostponedEnterTransition()
        true
      }
    }
  }

  private fun setSearchView(searchView: SearchView) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        query?.also { viewModel.search(it) }
        return true
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        newText?.also { viewModel.search(it) }
        return true
      }
    })
  }
}