@file:Suppress("UNCHECKED_CAST")
package com.hardik.hevintechnowebtask.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun <VH: ViewModel> viewModelFactory(initializer: () -> VH): ViewModelProvider.Factory{
    return object : ViewModelProvider.Factory{
        //        override fun <T : ViewModel> create(modelClass: Class<T>): T { return initializer as T }
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = initializer() // Call initializer to get the ViewModel instance
            if (modelClass.isAssignableFrom(viewModel::class.java)) {
                return viewModel as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}