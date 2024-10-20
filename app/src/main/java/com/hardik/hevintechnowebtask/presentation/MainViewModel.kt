package com.hardik.hevintechnowebtask.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hardik.hevintechnowebtask.common.Resource
import com.hardik.hevintechnowebtask.domain.model.UserModel
import com.hardik.hevintechnowebtask.domain.use_case.GetUserUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(private val getUserUseCase: GetUserUseCase) : ViewModel() {
    private val _state = MutableLiveData<UserListState<UserModel>>()
    val state: LiveData<UserListState<UserModel>> = _state

    private var skipPage = 0

    init {
        getUsers()
    }

    fun getUsers() {
        getUserUseCase(skip = skipPage).onEach { result: Resource<List<UserModel>> ->
            when(result){
                is Resource.Success -> {

                    _state.value = UserListState(users = result.data ?: emptyList())

                    skipPage += 10
                }
                is Resource.Error -> { _state.value = UserListState(error = result.message ?: "An unexpected error occurred") }
                is Resource.Loading -> { _state.value = UserListState(isLoading = true) }
            }

        }.launchIn(viewModelScope)
    }


//    private val _state1 = MutableStateFlow<UserListState<UserModel>>(UserListState(isLoading = true))
//    val state1: StateFlow<UserListState<UserModel>> = _state1
//
//    // Function to fetch users using flow
//    fun fetchUsers() {
//        viewModelScope.launch {
//            getUserUseCase().onEach { result: Resource<List<UserModel>> ->
//                when(result){
//                    is Resource.Success -> { _state1.value = UserListState(users = result.data ?: emptyList()) }
//                    is Resource.Error -> { _state1.value = UserListState(error = result.message ?: "An unexpected error occurred") }
//                    is Resource.Loading -> { _state1.value = UserListState(isLoading = true) }
//                }
//
//            }.launchIn(viewModelScope)
//        }
//    }

}

/*
class MainViewModelFactory: ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }
}*/

//todo: use like above or MainViewModelFactoryHelper class below

/*
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
}*/
