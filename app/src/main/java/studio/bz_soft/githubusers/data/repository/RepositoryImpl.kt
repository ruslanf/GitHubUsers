package studio.bz_soft.githubusers.data.repository

import androidx.lifecycle.LiveData
import org.koin.core.KoinComponent
import studio.bz_soft.githubusers.data.db.DbClient
import studio.bz_soft.githubusers.data.http.ApiClient
import studio.bz_soft.githubusers.data.http.Either
import studio.bz_soft.githubusers.data.http.safeRequest
import studio.bz_soft.githubusers.data.models.UserModel
import studio.bz_soft.githubusers.data.models.UsersModel
import studio.bz_soft.githubusers.data.models.db.User
import studio.bz_soft.githubusers.data.models.db.Users

class RepositoryImpl(
    private val storage: LocalStorage,
    private val database: DbClient,
    private val client: ApiClient
) : Repository, KoinComponent {
    override suspend fun getUsersList(): Either<Exception, List<UsersModel>> =
        safeRequest { client.getUsersList() }

    override suspend fun getInfoAboutUser(userName: String): Either<Exception, UserModel> =
        safeRequest { client.getUserInfo(userName) }

    override suspend fun insertUsers(users: Users) {
        database.insertUsers(users)
    }

    override suspend fun deleteUsers(users: Users) {
        database.deleteUsers(users)
    }

    override suspend fun updateUsers(users: Users) {
        database.updateUsers(users)
    }

    override fun getAllFromUsers(): LiveData<List<Users>> = database.getAllUsers()

    override suspend fun getListAllUsers(): List<Users> = database.getListAllUsers()

    override suspend fun getUsersRecords(userId: String): List<Users> =
        database.getUsersRecords(userId)

    override suspend fun getUsersRecord(userId: String): Users = database.getUsersRecord(userId)

    override suspend fun getListUsersRecords(listIDs: List<String>): List<Users> =
        listIDs.map { database.getUsersRecord(it) }

    override suspend fun updateListUsers(list: List<Users>): Either<Exception, Unit> =
        safeRequest { list.forEach { database.insertUsers(it) } }

    override suspend fun getAllUsers(): Either<Exception, List<Users>> =
        safeRequest { database.getAllFromUsers() }

    // User
    override suspend fun insertUser(user: User): Either<Exception, Unit> =
        safeRequest { database.insertUser(user) }

    override suspend fun deleteUser(user: User) {
        database.deleteUser(user)
    }

    override suspend fun updateUser(user: User) {
        database.updateUser(user)
    }

    override fun getAllFromUser(): LiveData<List<User>> = database.getAllFromUser()

    override suspend fun getUserRecords(userId: String): List<User> =
        database.getUserRecords(userId)

    override suspend fun getUserInfo(userId: String): Either<Exception, User> =
        safeRequest { database.getUserInfo(userId) }

    override fun getUser(userName: String): User = database.getUser(userName)
}