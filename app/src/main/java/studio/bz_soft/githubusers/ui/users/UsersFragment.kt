package studio.bz_soft.githubusers.ui.users

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.android.synthetic.main.fragment_users.progressBar
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import studio.bz_soft.githubusers.R
import studio.bz_soft.githubusers.data.models.db.Users
import studio.bz_soft.githubusers.data.models.vm.UsersListVM
import studio.bz_soft.githubusers.root.Constants.KEY_USER_NAME
import studio.bz_soft.githubusers.root.delegated.DelegateAdapter
import studio.bz_soft.githubusers.root.scrollToPosition
import studio.bz_soft.githubusers.ui.root.RootActivity
import kotlin.coroutines.CoroutineContext

class UsersFragment : Fragment(), CoroutineScope {

    private val usersListVM by inject<UsersListVM>()

    private var job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    private val usersListAdapter = DelegateAdapter(UsersListItemDelegate { users ->
        val bundle = bundleOf(
            KEY_USER_NAME to users.userName
        )
        view?.findNavController()?.navigate(R.id.action_usersFragment_to_userInfoFragment, bundle)
    })

    private var recyclerViewState: Parcelable? = null
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_users, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            usersRV.apply {
                adapter = usersListAdapter
                layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
                recyclerViewState?.apply {
                    layoutManager?.onRestoreInstanceState(recyclerViewState)
                    scrollToPosition(usersRV, position)
                }
            }
            swipeRefresh.setOnRefreshListener { refreshListener(this) }
            usersListVM.progress.observe(viewLifecycleOwner, Observer { render(it) })
            usersListVM.usersList.observe(viewLifecycleOwner, Observer { renderUsersList(it) })
            fetchUsers()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as RootActivity).mainBottomNavigationMenu.visibility = View.VISIBLE
    }

    private fun fetchUsers() {
        launch(SupervisorJob(job) + Dispatchers.IO) { usersListVM.fetchUsers() }
    }

    private fun renderUsersList(users: List<Users>) {
        usersListAdapter.apply {
            items.clear()
            items.addAll(
                users.map { UsersListElement.UsersListItem(it) }
            )
            notifyDataSetChanged()
        }
    }

    private fun refreshListener(v: View) {
        v.apply {
            fetchUsers()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun render(progress: Int) {
        progressBar.visibility = when (progress > 0) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }
}