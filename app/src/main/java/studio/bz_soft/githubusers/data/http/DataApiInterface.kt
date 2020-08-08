package studio.bz_soft.githubusers.data.http

import retrofit2.http.*
import studio.bz_soft.githubusers.data.models.*

interface DataApiInterface {

    @GET("/users")
    suspend fun getUsers(): List<UsersModel>

    @GET("/users/{username}")
    suspend fun getUserInfo(@Path("username") userName: String): UserModel
}