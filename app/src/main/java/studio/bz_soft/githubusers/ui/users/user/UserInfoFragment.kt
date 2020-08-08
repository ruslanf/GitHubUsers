package studio.bz_soft.githubusers.ui.users.user

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.api.load
import coil.transform.CircleCropTransformation
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.fragment_user_info.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import studio.bz_soft.githubusers.R
import studio.bz_soft.githubusers.data.models.db.User
import studio.bz_soft.githubusers.data.models.vm.UserInfoVM
import studio.bz_soft.githubusers.root.Constants.KEY_USER_NAME
import studio.bz_soft.githubusers.ui.root.RootActivity
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

class UserInfoFragment : Fragment(), CoroutineScope {

    private val userInfo by inject<UserInfoVM>()

    private var job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    private var userName by Delegates.notNull<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(KEY_USER_NAME)?.let { userName = it }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_user_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            linkTV.setOnClickListener { linkClickListener(linkTV.text.toString()) }
            launch(SupervisorJob(job) + Dispatchers.IO) { userInfo.fetchUserInfo(userName) }
            userInfo.progress.observe(viewLifecycleOwner, Observer { render(it) })
            userInfo.userInfo.observe(viewLifecycleOwner, Observer { displayUserInfo(this, it) })
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as RootActivity).mainBottomNavigationMenu.visibility = View.GONE
    }

    private fun displayUserInfo(v: View, user: User) {
        v.apply {
            avatarIV.load(user.avatar) {
                size(64)
                placeholder(R.drawable.ic_no_user_light)
                fallback(R.drawable.ic_no_user_light)
                transformations(CircleCropTransformation())
            }
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

    private fun render(progress: Int) {
        progressBar.visibility = when (progress > 0) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }
}