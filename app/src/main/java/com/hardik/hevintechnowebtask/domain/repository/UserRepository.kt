package com.hardik.hevintechnowebtask.domain.repository

import com.hardik.hevintechnowebtask.data.remote.dto.UserDto

interface UserRepository {
    suspend fun getUsers(skip : Int): UserDto
//    suspend fun insertUser(appContext: Context, user: UserModel)//todo: insertUser in database
}