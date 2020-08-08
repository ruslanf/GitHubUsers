package studio.bz_soft.githubusers.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import studio.bz_soft.githubusers.data.models.db.User
import studio.bz_soft.githubusers.data.models.db.Users

interface DbClient {
    suspend fun insertUsers(users: Users)
    suspend fun deleteUsers(users: Users)
    suspend fun updateUsers(users: Users)
    fun getAllUsers(): LiveData<List<Users>>
    suspend fun getAllFromUsers(): List<Users>
    suspend fun getListAllUsers(): List<Users>
    suspend fun getUsersRecords(userId: String): List<Users>
    suspend fun getUsersRecord(userId: String): Users

    suspend fun insertUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun updateUser(user: User)
    fun getAllFromUser(): LiveData<List<User>>
    suspend fun getUserRecords(userId: String): List<User>
    suspend fun getUserInfo(userId: String): User

    fun getUser(userName: String): User
}