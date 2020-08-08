package studio.bz_soft.githubusers.data.models.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.bz_soft.githubusers.data.http.Left
import studio.bz_soft.githubusers.data.http.Right
import studio.bz_soft.githubusers.data.models.UserModel
import studio.bz_soft.githubusers.data.models.db.User
import studio.bz_soft.githubusers.data.repository.Repository
import studio.bz_soft.githubusers.root.Constants.EMPTY_STRING
import studio.bz_soft.githubusers.root.event.Event

class UserInfoVM(
    private val repository: Repository
) : ViewModel() {
    val progress = MutableLiveData<Int>()
    val errors = MutableLiveData<Event<Exception>>()

    val userInfo = MutableLiveData<User>()
    private var counter = 0

    suspend fun fetchUserInfo(userName: String) {
        counter += 1
        progress.postValue(counter)
        when (val r = repository.getInfoAboutUser(userName)) {
            is Right -> {
                saveUserInfo(r.value)
            }
            is Left -> errors.value = Event(r.value)
        }
        counter -= 1
        progress.postValue(counter)
    }

    private suspend fun saveUserInfo(model: UserModel) {
        val known = model.name?.let { repository.getUser(it) }
        known?.let {
            displayUser(known)
        } ?: updateUser(User(
                login = model.login,
                userName = model.name ?: EMPTY_STRING,
                userId = model.nodeId,
                avatar = model.avatarUrl ?: EMPTY_STRING,
                htmlUrl = model.htmlUrl ?: EMPTY_STRING,
                location = model.location ?: EMPTY_STRING,
                followers = model.followers ?: 0,
                following = model.following ?: 0
            )
        )
    }

    private suspend fun updateUser(user: User) {
        when (val r = repository.insertUser(user)) {
            is Left -> errors.value = Event(r.value)
            is Right -> displayUser(user)
        }
    }

    private suspend fun displayUser(user: User) {
        when (val r = repository.getUserInfo(user.userId)) {
            is Left -> errors.postValue(Event(r.value))
            is Right -> userInfo.postValue(r.value)
        }
    }
}