package com.hardik.hevintechnowebtask.data.repository

import android.content.Context
import com.hardik.hevintechnowebtask.common.Constants.BASE_TAG
import com.hardik.hevintechnowebtask.data.remote.api.ApiInterface
import com.hardik.hevintechnowebtask.data.remote.dto.UserDto
import com.hardik.hevintechnowebtask.domain.repository.UserRepository


class UserRepositoryImpl(private val appContext : Context, private val apiInterface: ApiInterface):
    UserRepository {
    private val TAG = BASE_TAG + UserRepositoryImpl::class.java.simpleName

    override suspend fun getUsers(skip : Int): UserDto {
        return apiInterface.getUsers(skip = skip)

//        // Check if the database has users
//        val usersFromDb = AppDatabase.getDatabase(appContext).userDao().getAllUsers()
//        return if (usersFromDb.isNotEmpty()) {
//            Log.d(TAG, "getUsers: From the database")
//            // If there are users in the database, return them
//            usersFromDb.map { it.toUserDto() }
//        } else {
//            Log.d(TAG, "getUsers: calling api for get user data")
//            // If the database is empty, fetch from API
//            val usersFromApi = apiInterface.getUsers()
//            if (usersFromApi.isNotEmpty()) {
//                Log.d(TAG, "getUsers: insert data in user table")
//
//                // Store the fetched users in the database
//                AppDatabase.getDatabase(appContext).userDao().insertUsers(usersFromApi.map { it.toUserModel() })
//            }
//            // Return users from API
//            usersFromApi
//        }

    }

//    override suspend fun insertUser(appContext: Context, user: UserModel) {  AppDatabase.getDatabase(appContext).userDao().insertUser(user) }
}