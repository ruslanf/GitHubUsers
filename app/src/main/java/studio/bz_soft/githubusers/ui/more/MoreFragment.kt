package studio.bz_soft.githubusers.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_root.*
import studio.bz_soft.githubusers.R
import studio.bz_soft.githubusers.ui.root.RootActivity

class MoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_more, container, false)

    override fun onResume() {
        super.onResume()
        (activity as RootActivity).mainBottomNavigationMenu.visibility = View.VISIBLE
    }
}