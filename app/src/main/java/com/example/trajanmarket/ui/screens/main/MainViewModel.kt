package com.example.trajanmarket.ui.screens.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    
    private val _bottomNavbarIndex = MutableStateFlow(0)
    val bottomNavbarIndex = _bottomNavbarIndex
    
    fun onChangeBottomNavbarIndex(index: Int) {
        _bottomNavbarIndex.update { index }
    }
    
}