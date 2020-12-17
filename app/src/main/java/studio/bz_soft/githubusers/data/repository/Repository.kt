package studio.bz_soft.githubusers.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import studio.bz_soft.githubusers.data.http.Either
import studio.bz_soft.githubusers.data.models.UserModel
import studio.bz_soft.githubusers.data.models.UsersModel
import studio.bz_soft.githubusers.data.models.db.User
import studio.bz_soft.githubusers.data.models.db.Users

interface Repository {
    suspend fun getUsersList(): Either<Exception, List<UsersModel>>
    suspend fun getInfoAboutUser(userName: String): Either<Exception, UserModel>

    // DB
    suspend fun insertUsers(users: Users)
    suspend fun deleteUsers(users: Users)
    suspend fun updateUsers(users: Users)
    fun getAllFromUsers(): LiveData<List<Users>>
    suspend fun getListAllUsers(): List<Users>
    suspend fun getUsersRecords(userId: String): List<Users>
    fun watchUsers(): Flow<List<Users>>

    suspend fun getUsersRecord(userId: String): Users
    suspend fun getListUsersRecords(listIDs: List<String>): List<Users>
    suspend fun updateListUsers(list: List<Users>): Either<Exception, Unit>
    suspend fun getAllUsers(): Either<Exception, List<Users>>

    suspend fun insertUser(user: User): Either<Exception, Unit>
    suspend fun deleteUser(user: User)
    suspend fun updateUser(user: User)
    fun getAllFromUser(): LiveData<List<User>>
    suspend fun getUserRecords(userId: String): List<User>
    suspend fun getUserInfo(userId: String): Either<Exception, User>

    fun getUser(userName: String): User
}