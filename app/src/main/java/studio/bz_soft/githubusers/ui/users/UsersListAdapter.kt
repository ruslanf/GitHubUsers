package studio.bz_soft.githubusers.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import coil.transform.CircleCropTransformation
import kotlinx.android.synthetic.main.cell_users.view.*
import studio.bz_soft.githubusers.R
import studio.bz_soft.githubusers.data.models.db.Users
import studio.bz_soft.githubusers.root.delegated.AdapterDelegateInterface
import studio.bz_soft.githubusers.root.delegated.BaseHolder

sealed class UsersListElement {
    data class UsersListItem(val users: Users): UsersListElement()
}

class CompaniesItemHolder(v: View, val onClick: (Users) -> Unit): BaseHolder<UsersListElement>(v) {

    override fun bindModel(item: UsersListElement) {
        super.bindModel(item)
        when (item) {
            is UsersListElement.UsersListItem -> itemView.apply {
                val image = "${item.users.avatar}?w=360"
                userIV.load(image) {
                    size(64)
                    placeholder(R.drawable.ic_no_user_light)
                    fallback(R.drawable.ic_no_user_light)
                    transformations(CircleCropTransformation())
                }
                userTitleTV.text = item.users.userName
                setOnClickListener { onClick(item.users) }
            }
        }
    }
}

class UsersListItemDelegate(private val onClick: (Users) -> Unit):
    AdapterDelegateInterface<UsersListElement> {

    override fun isForViewType(items: List<UsersListElement>, position: Int): Boolean {
        return items[position] is UsersListElement.UsersListItem
    }

    override fun createViewHolder(parent: ViewGroup): BaseHolder<UsersListElement> {
        return CompaniesItemHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_users, parent, false),
            onClick
        )
    }

    override fun bindViewHolder(items: List<UsersListElement>, position: Int, holder: BaseHolder<UsersListElement>) {
        holder.bindModel(items[position])
    }
}