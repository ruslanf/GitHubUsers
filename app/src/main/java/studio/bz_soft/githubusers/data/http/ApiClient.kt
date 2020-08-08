package studio.bz_soft.githubusers.data.http

import studio.bz_soft.githubusers.data.models.*

interface ApiClient {
    suspend fun getUsersList(): List<UsersModel>
    suspend fun getUserInfo(userName: String): UserModel
}