package studio.bz_soft.githubusers.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_root.*
import studio.bz_soft.githubusers.R
import studio.bz_soft.githubusers.ui.root.RootActivity

abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI(view)
    }

    override fun onResume() {
        super.onResume()
        activity?.let { fa ->
            if (fa is RootActivity) destinationListener(fa)
        }
    }

    abstract fun setupUI(v: View)

    fun navigateUp() {
        navigateController()?.navigateUp()
    }

    fun navigateTo(@IdRes id: Int) {
        navigateController()?.navigate(id)
    }

    fun navigateTo(@IdRes id: Int, bundle: Bundle) {
        navigateController()?.navigate(id, bundle)
    }

    fun navigateBackTo(@IdRes id: Int, inclusive: Boolean) {
        navigateController()?.popBackStack(id, inclusive)
    }

    private fun navigateController(): NavController? = activity?.let { fa ->
        return@let if (fa is RootActivity) Navigation.findNavController(fa, R.id.nav_host_fragment)
        else view?.findNavController()
    }

    private fun destinationListener(fa: FragmentActivity) {
        navigateController()?.addOnDestinationChangedListener { _, destination, _ ->
            fa.mainBottomNavigationMenu.visibility = when (destination.id) {
                R.id.menuUsersList -> View.VISIBLE
                R.id.menuMore -> View.VISIBLE
                R.id.menuSettings -> View.VISIBLE
                else -> View.GONE
            }
        }
    }
}