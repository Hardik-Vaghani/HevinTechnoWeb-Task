package com.hardik.hevintechnowebtask.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hardik.hevintechnowebtask.domain.model.UserModel

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserModel) //Todo: UserModel is define in domain/model/UserModel or data/database/entities/UserModel(UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserModel>) // This method handles the list

    @Update
    suspend fun updateUser(user: UserModel)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserModel?




    // Get all users as LiveData with optional sorting
    @Query("SELECT * FROM users ORDER BY :sortField ,:sortOrder")
    fun getAllUsers(sortField: String = "id", sortOrder: String = "ASC"): LiveData<List<UserModel>>

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<UserModel>> //Todo: Get all users as LiveData

    @Query("SELECT * FROM users ORDER BY firstName ASC")
    fun getAllUsersSortedByFirstNameAsc(): LiveData<List<UserModel>>

    @Query("SELECT * FROM users ORDER BY firstName DESC")
    fun getAllUsersSortedByFirstNameDesc(): LiveData<List<UserModel>>

    @Query("SELECT * FROM users ORDER BY lastName ASC")
    fun getAllUsersSortedByLastNameAsc(): LiveData<List<UserModel>>

    @Query("SELECT * FROM users ORDER BY lastName DESC")
    fun getAllUsersSortedByLastNameDesc(): LiveData<List<UserModel>>

    @Query("SELECT * FROM users ORDER BY email ASC")
    fun getAllUsersSortedByEmailAsc(): LiveData<List<UserModel>>

    @Query("SELECT * FROM users ORDER BY email DESC")
    fun getAllUsersSortedByEmailDesc(): LiveData<List<UserModel>>

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsersSortedByIdAsc(): LiveData<List<UserModel>>

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsersSortedByIdDesc(): LiveData<List<UserModel>>




    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)

    @Delete
    suspend fun deleteUser(user: UserModel)

    // Delete all users
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}


enum class SortField(val value: String) {
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email"),
    ID("id")
}

enum class SortOrder(val value: String) {
    ASC("ASC"),
    DESC("DESC")
}
