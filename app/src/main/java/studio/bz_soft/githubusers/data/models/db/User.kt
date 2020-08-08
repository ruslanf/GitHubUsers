package studio.bz_soft.githubusers.data.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "avatar") val avatar: String,
    @ColumnInfo(name = "html_url") val htmlUrl: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "followers") val followers: Int,
    @ColumnInfo(name = "following") val following: Int

)