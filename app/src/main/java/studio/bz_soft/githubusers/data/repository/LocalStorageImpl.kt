package studio.bz_soft.githubusers.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson

class LocalStorageImpl(
    private val preferences: SharedPreferences
) : LocalStorage {

    private val gson = Gson()

}