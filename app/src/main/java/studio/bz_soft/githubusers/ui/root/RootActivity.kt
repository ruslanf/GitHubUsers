package studio.bz_soft.githubusers.ui.root

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import studio.bz_soft.githubusers.R
import studio.bz_soft.githubusers.data.models.vm.UsersListVM
import kotlin.coroutines.CoroutineContext

class RootActivity : AppCompatActivity(), CoroutineScope {

    private var job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    private val usersListVM by inject<UsersListVM>()

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        setSupportActionBar(toolbar)

        initialize()

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.menuUsersList, R.id.menuMore, R.id.menuSettings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        mainBottomNavigationMenu.setupWithNavController(navController)
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

    private fun initialize() {
        usersListVM.progress.observe(this, Observer { render(it) })
    }

    private fun syncButtonListener() {
        launch(SupervisorJob(job) + Dispatchers.IO) { usersListVM.fetchUsers() }
    }

    private fun render(progress: Int) {
        progressBar.visibility = when (progress > 0) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }
}
