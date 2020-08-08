package studio.bz_soft.githubusers.data.models.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import studio.bz_soft.githubusers.data.http.Left
import studio.bz_soft.githubusers.data.http.Right
import studio.bz_soft.githubusers.data.models.UsersModel
import studio.bz_soft.githubusers.data.models.db.Users
import studio.bz_soft.githubusers.data.repository.Repository
import studio.bz_soft.githubusers.root.event.Event

class UsersListVM(
    private val repository: Repository
) : ViewModel() {

    val progress = MutableLiveData<Int>()
    val errors = MutableLiveData<Event<Exception>>()

    val usersList = MutableLiveData<List<Users>>()
    private var counter = 0

    suspend fun fetchUsers() {
        counter += 1
        progress.postValue(counter)
        when (val r = repository.getUsersList()) {
            is Right -> { saveUsers(r.value) }
            is Left -> errors.value = Event(r.value)
        }
        counter -= 1
        progress.postValue(counter)
    }

    private suspend fun saveUsers(found: List<UsersModel>) {
        val foundIDs = found.map { it.nodeid }
        val unknown = repository.getListUsersRecords(foundIDs)
        try {
            val knownUsers = found.filter { user -> unknown.any { user.nodeid == it.userId } }
            val unknownUsers = found.filter { user -> unknown.none { user.nodeid == it.userId } }

            updateUsers(unknownUsers)
        } catch (ex: NullPointerException) {
            updateUsers(found)
        }
    }

    private suspend fun updateUsers(list: List<UsersModel>) {
        when (val r = repository.updateListUsers(
            list.map {
                Users(userId = it.nodeid, userName = it.login, avatar = it.avatarUrl)
            })) {
            is Left -> errors.value = Event(r.value)
            is Right -> displayAllUsers()
        }
    }

    private suspend fun displayAllUsers() {
        when (val r = repository.getAllUsers()) {
            is Left -> errors.postValue(Event(r.value))
            is Right -> usersList.postValue(r.value)
        }
    }
}