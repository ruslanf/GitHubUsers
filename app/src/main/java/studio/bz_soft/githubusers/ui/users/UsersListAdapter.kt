package studio.bz_soft.githubusers.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import studio.bz_soft.githubusers.R
import studio.bz_soft.githubusers.data.models.db.Users
import studio.bz_soft.githubusers.databinding.CellUsersBinding
import studio.bz_soft.githubusers.root.delegated.AdapterDelegateInterface
import studio.bz_soft.githubusers.root.delegated.BaseHolder
import studio.bz_soft.githubusers.root.showImage

sealed class UsersListElement {
    data class UsersListItem(val users: Users): UsersListElement()
}

class CompaniesItemHolder(v: View, val onClick: (Users) -> Unit): BaseHolder<UsersListElement>(v) {

    override fun bindModel(item: UsersListElement) {
        super.bindModel(item)
        CellUsersBinding.bind(itemView).apply {
            when (item) {
                is UsersListElement.UsersListItem -> itemView.apply {
                    showImage(userIV, "${item.users.avatar}?w=360")
                    userTitleTV.text = item.users.userName
                    setOnClickListener { onClick(item.users) }
                }
            }
        }
    }
}

class UsersListItemDelegate(private val onClick: (Users) -> Unit):
    AdapterDelegateInterface<UsersListElement> {

    override fun isForViewType(items: List<UsersListElement>, position: Int): Boolean =
        items[position] is UsersListElement.UsersListItem

    override fun createViewHolder(parent: ViewGroup): BaseHolder<UsersListElement> =
        CompaniesItemHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_users, parent, false),
            onClick
        )

    override fun bindViewHolder(items: List<UsersListElement>, position: Int, holder: BaseHolder<UsersListElement>) {
        holder.bindModel(items[position])
    }
}