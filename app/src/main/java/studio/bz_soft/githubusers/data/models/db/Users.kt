package studio.bz_soft.githubusers.data.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class Users(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "avatar") val avatar: String
)