package studio.bz_soft.githubusers.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import studio.bz_soft.githubusers.data.models.db.User

@Dao
interface UserDao : BaseDao<User> {
    @Query("Select * from user")
    fun getAllFromUser(): LiveData<List<User>>

    @Query("Select * from user where user_id = :userId")
    suspend fun getRecords(userId: String): List<User>

    @Query("Select * from user where user_id = :userId")
    suspend fun getUserInfo(userId: String): User

    @Query("Select * from user where user_name = :userName")
    fun getInfo(userName: String): User
}