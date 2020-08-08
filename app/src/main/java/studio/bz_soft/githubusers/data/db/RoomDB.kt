package studio.bz_soft.githubusers.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import studio.bz_soft.githubusers.data.db.converters.Converters
import studio.bz_soft.githubusers.data.models.db.User
import studio.bz_soft.githubusers.data.models.db.Users
import studio.bz_soft.githubusers.root.Constants.DB_NAME

@Database(entities = [Users::class, User::class],
    version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RoomDB : RoomDatabase() {

    abstract fun usersDao() : UsersDao
    abstract fun userDao() : UserDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getDataBase(context: Context): RoomDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: createDB(context).also { INSTANCE = it }
            }

        private fun createDB(context: Context) = Room
            .databaseBuilder(context.applicationContext, RoomDB::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}