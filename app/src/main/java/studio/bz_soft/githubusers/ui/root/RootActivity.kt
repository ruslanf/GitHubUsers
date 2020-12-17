package studio.bz_soft.githubusers.ui.root

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import studio.bz_soft.githubusers.R
import studio.bz_soft.githubusers.data.models.vm.UsersListVM
import studio.bz_soft.githubusers.databinding.ActivityRootBinding
import studio.bz_soft.githubusers.root.showProgressBar

@ExperimentalCoroutinesApi
class RootActivity : AppCompatActivity() {

    private val usersListVM by inject<UsersListVM>()

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        val binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        initialize(binding)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.menuUsersList, R.id.menuMore, R.id.menuSettings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
       binding. mainBottomNavigationMenu.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_main_menu, menu)
        if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        NavigationUI.onNavDestinationSelected(item, navController)
        when (item.itemId) {
            R.id.menuSync -> syncButtonListener()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialize(binding: ActivityRootBinding) {
        usersListVM.progress.observe(this) { binding.progressBar.showProgressBar(it) }
    }

    private fun syncButtonListener() {
        usersListVM.fetchUsers()
    }
}
