package com.hardik.hevintechnowebtask.domain.use_case

import android.util.Log
import androidx.lifecycle.asFlow
import com.hardik.hevintechnowebtask.common.Resource
import com.hardik.hevintechnowebtask.data.database.dao.SortOrder
import com.hardik.hevintechnowebtask.data.remote.dto.toUserModel
import com.hardik.hevintechnowebtask.di.AppModule
import com.hardik.hevintechnowebtask.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

//class GetUserUseCase(/*private val appModule: AppModule*/private val repository: UserRepository) {
class GetUserUseCase(private val appModule: AppModule) {
    private val TAG = GetUserUseCase::class.java.simpleName

    operator fun invoke(skip : Int): Flow<Resource<List<UserModel>>> = flow {
        try {

            // Step 1: Fetch users from the repository
            val userDto = appModule.userRepository.getUsers(skip = skip)
            val users = userDto.users.map { it.toUserModel() }

            // Step 2: Insert users into the database
            appModule.userDao.insertUsers(users)

            // Step 3: Retrieve users from the database
            val usersFromDB = appModule.userDao.getAllUsers().asFlow().first() // Use 'first()' to get the first value of LiveData
            Log.d(TAG, "invoke: \n${usersFromDB}")

            // Step 4: Emit the success result
            emit(Resource.Success(usersFromDB ?: emptyList()))

        } catch(e: HttpException) {
            emit(Resource.Error<List<UserModel>>(e.localizedMessage ?: "An unexpected error occurred"))
        } catch(e: java.io.IOException) {
            emit(Resource.Error<List<UserModel>>("Couldn't reach server. Check your internet connection."))
        }
    }

    fun getAllUsersSortedByName(sortOrder: SortOrder) =
        when(sortOrder){
            SortOrder.ASC->{appModule.userDao.getAllUsersSortedByFirstNameAsc()}
            SortOrder.DESC->{appModule.userDao.getAllUsersSortedByFirstNameDesc()}
        }

    fun getAllUsersSortedByEmail(sortOrder: SortOrder) =
        when(sortOrder){
            SortOrder.ASC->{appModule.userDao.getAllUsersSortedByEmailAsc()}
            SortOrder.DESC->{appModule.userDao.getAllUsersSortedByEmailDesc()}
        }

    fun getAllUsers() = appModule.userDao.getAllUsers()
}