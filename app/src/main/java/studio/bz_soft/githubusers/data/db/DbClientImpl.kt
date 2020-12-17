package studio.bz_soft.githubusers.data.db

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import studio.bz_soft.githubusers.data.models.db.User
import studio.bz_soft.githubusers.data.models.db.Users

class DbClientImpl(application: Application) : DbClient {

    private val db by lazy { RoomDB.getDataBase(application) }
    private val usersDao by lazy { db.usersDao() }
    private val userDao by lazy { db.userDao() }

    override suspend fun insertUsers(users: Users) {
        usersDao.insert(users)
    }

    override suspend fun deleteUsers(users: Users) {
        usersDao.delete(users)
    }

    override suspend fun updateUsers(users: Users) {
        usersDao.update(users)
    }

    override fun getAllUsers(): LiveData<List<Users>> = usersDao.getAllUsers()

    override fun watchUsers(): Flow<List<Users>> = usersDao.watchUsers()

    override suspend fun getAllFromUsers(): List<Users> = usersDao.getAllFromUsers()

    override suspend fun getListAllUsers(): List<Users> = usersDao.getListAllUsers()

    override suspend fun getUsersRecords(userId: String): List<Users> = usersDao.getRecords(userId)

    override suspend fun getUsersRecord(userId: String): Users = usersDao.getRecord(userId)

    override suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    override suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }

    override suspend fun updateUser(user: User) {
        userDao.update(user)
    }

    override fun getAllFromUser(): LiveData<List<User>> = userDao.getAllFromUser()

    override suspend fun getUserRecords(userId: String): List<User> = userDao.getRecords(userId)

    override suspend fun getUserInfo(userId: String): User = userDao.getUserInfo(userId)

    override fun getUser(userName: String): User = userDao.getInfo(userName)
}