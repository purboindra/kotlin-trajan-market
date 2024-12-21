package com.example.trajanmarket.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.domain.usecases.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class SearchViewModel @Inject constructor(private val productUseCase: GetProductUseCase) :
    ViewModel() {
    private val _searchState = MutableStateFlow<State<Product>>(State.Idle)
    val searchState: StateFlow<State<Product>> = _searchState
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery
    
    fun onQueryChanged(query: String) {
        _searchQuery.value = query
    }
    
    suspend fun resetState() {
        _searchState.emit(State.Idle)
    }
    
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery.debounce(300).filter {
                it.isNotEmpty()
            }.distinctUntilChanged().flatMapLatest { query ->
                productUseCase.searchProduct(query)
            }.collectLatest { state ->
                _searchState.value = state
            }
        }
    }
    
    init {
        observeSearchQuery()
    }
}