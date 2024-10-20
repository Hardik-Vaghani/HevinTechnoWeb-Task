package com.hardik.hevintechnowebtask.data.remote.api

import com.hardik.hevintechnowebtask.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/users")
    suspend fun getUsers(
        @Query("limit") limit: Int= 10,
        @Query("skip") skip: Int,
    ): UserDto

    /**
     * Without suspend keyword,it is necessary to use Call<Post>,
     * With suspend keyword, It isn't necessary to use Call<Post>, You have two option Call<Post> or Post.
     * */


}