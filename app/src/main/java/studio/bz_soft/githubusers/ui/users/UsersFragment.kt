package studio.bz_soft.githubusers.ui.users

import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import studio.bz_soft.githubusers.R
import studio.bz_soft.githubusers.data.models.db.Users
import studio.bz_soft.githubusers.data.models.vm.UsersListVM
import studio.bz_soft.githubusers.databinding.FragmentUsersBinding
import studio.bz_soft.githubusers.root.Constants.KEY_USER_NAME
import studio.bz_soft.githubusers.root.delegated.DelegateAdapter
import studio.bz_soft.githubusers.root.setRecyclerView
import studio.bz_soft.githubusers.root.showProgressBar
import studio.bz_soft.githubusers.ui.BaseFragment

@ExperimentalCoroutinesApi
class UsersFragment : BaseFragment(R.layout.fragment_users) {

    private val usersListVM by inject<UsersListVM>()

    private val usersListAdapter = DelegateAdapter(UsersListItemDelegate { users ->
        val bundle = bundleOf(
            KEY_USER_NAME to users.userName
        )
        navigateTo(R.id.action_usersFragment_to_userInfoFragment, bundle)
    })

    override fun setupUI(v: View) {
        FragmentUsersBinding.bind(v).apply {
            usersRV.setRecyclerView(this.root, usersListAdapter)
            swipeRefresh.setOnRefreshListener { refreshListener(this) }
            with(usersListVM) {
                progress.observe(viewLifecycleOwner) { progressBar.showProgressBar(it) }
                usersList.observe(viewLifecycleOwner) { renderUsersList(it) }
            }
            lifecycleScope.launch {
                launch { usersListVM.watchUsersList.collect { renderUsersList(it) } }
            }
        }
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

    private fun refreshListener(binding: FragmentUsersBinding) {
        binding.apply {
            usersListVM.fetchUsers()
            swipeRefresh.isRefreshing = false
        }
    }
}