package studio.bz_soft.githubusers.ui.users.user

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import studio.bz_soft.githubusers.R
import studio.bz_soft.githubusers.data.models.db.User
import studio.bz_soft.githubusers.data.models.vm.UserInfoVM
import studio.bz_soft.githubusers.databinding.FragmentUserInfoBinding
import studio.bz_soft.githubusers.root.Constants.KEY_USER_NAME
import studio.bz_soft.githubusers.root.showImage
import studio.bz_soft.githubusers.root.showProgressBar
import studio.bz_soft.githubusers.ui.BaseFragment
import kotlin.properties.Delegates

class UserInfoFragment : BaseFragment(R.layout.fragment_user_info) {

    private val userInfo by inject<UserInfoVM>()

    private var userName by Delegates.notNull<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(KEY_USER_NAME)?.let { userName = it }
    }

    override fun setupUI(v: View) {
        FragmentUserInfoBinding.bind(v).apply {
            linkTV.setOnClickListener { linkClickListener(linkTV.text.toString()) }
            with(userInfo) {
                progress.observe(viewLifecycleOwner, { progressBar.showProgressBar(it) })
                userInfo.observe(viewLifecycleOwner, { displayUserInfo(this@apply, it) })
            }
            lifecycleScope.launch {
                launch { userInfo.fetchUserInfo(userName) }
            }
        }
    }

    private fun displayUserInfo(binding: FragmentUserInfoBinding, user: User) {
        binding.apply {
            showImage(avatarIV, user.avatar)
            loginTV.text = user.login
            usernameTV.text = user.userName
            locationTV.text = user.location
            linkTV.text = user.htmlUrl
            followersTV.text = user.followers.toString()
            followingTV.text = user.following.toString()
        }
    }

    private fun linkClickListener(url: String) {
        val customTabs = CustomTabsIntent.Builder().build()
        if (url.isNotEmpty()) customTabs.launchUrl(context, Uri.parse(url))
    }
}