package com.vk.usersapp.feature.feed.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vk.usersapp.R
import com.vk.usersapp.core.ViewModelProvider
import com.vk.usersapp.databinding.FragmentProductListBinding
import com.vk.usersapp.feature.feed.presentation.ProductsListAction
import com.vk.usersapp.feature.feed.presentation.ProductListFeature
import com.vk.usersapp.feature.feed.presentation.ProductListViewState
import kotlinx.coroutines.launch

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val adapter: ProductListAdapter by lazy { ProductListAdapter() }
    private lateinit var recycler: RecyclerView
    private lateinit var errorView: TextView
    private lateinit var loaderView: ProgressBar

    var feature: ProductListFeature? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = binding.recycler
        errorView = binding.error
        loaderView = binding.loader

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(view.context)
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount <= lastVisibleItem + 2)
                    if (!feature!!.isAllUsersLoaded())
                        feature?.submitAction(ProductsListAction.LoadMore)
            }
        })

        feature = ViewModelProvider.obtainFeature {
            ProductListFeature()
        }
        binding.viewModel = feature
        binding.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                feature?.viewStateFlow?.collect {
                    renderState(it)
                }
            }
        }

        feature?.submitAction(ProductsListAction.Init)
    }

    override fun onDestroy() {
        feature?.let {
            if (activity?.isFinishing == true) {
                ViewModelProvider.destroyFeature(it.javaClass)
            }
        }
        _binding = null
        super.onDestroy()
    }

    private fun renderState(viewState: ProductListViewState) {
        when (viewState) {
            is ProductListViewState.Error -> {
                errorView.isVisible = true
                errorView.text = viewState.errorText
                loaderView.isVisible = false
                recycler.isVisible = false
            }
            is ProductListViewState.List -> {
                loaderView.isVisible = false
                if (viewState.itemsList.isEmpty()) {
                    errorView.isVisible = true
                    recycler.isVisible = false
                    errorView.text = requireContext().getString(R.string.nothing_found)
                } else {
                    errorView.isVisible = false
                    recycler.isVisible = true
                    adapter.setUsers(viewState.itemsList)
                }
            }
            ProductListViewState.Loading -> {
                errorView.isVisible = false
                loaderView.isVisible = true
                recycler.isVisible = false
            }
        }
    }
}