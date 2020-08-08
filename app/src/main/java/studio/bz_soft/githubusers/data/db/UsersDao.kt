package studio.bz_soft.githubusers.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import studio.bz_soft.githubusers.data.models.db.Users

@Dao
interface UsersDao : BaseDao<Users> {
    @Query("Select * from users")
    fun getAllUsers(): LiveData<List<Users>>

    @Query("Select * from users")
    fun getAllFromUsers(): List<Users>

    @Query("Select * from users")
    suspend fun getListAllUsers(): List<Users>

    @Query("Select * from users where user_id = :userId")
    suspend fun getRecords(userId: String): List<Users>

    @Query("Select * from users where user_id = :userId")
    suspend fun getRecord(userId: String): Users
}