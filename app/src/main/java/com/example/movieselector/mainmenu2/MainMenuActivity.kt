package com.example.movieselector.mainmenu2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.movieselector.R
import com.example.movieselector.databinding.ActivityMainMenuBinding
import com.example.movieselector.mainmenu2.ui.fratgmentmenu.UserListFragment
import com.example.movieselector.testdir.uid


class MainMenuActivity : AppCompatActivity() {

    val TAG = "MainMenuActivity"
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //UserListFragment().arguments?.putString("uid", intent.extras!!.get("uid").toString())
        setContentView(R.layout.main_menu)
        Log.d(TAG, "Start3")
//---

//---

        //UserListFragment().arguments?.putString("uid", uid)

        /*binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMainMenu.toolbar)

        binding.appBarMainMenu.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main_menu)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )*/
       // setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }*/

        /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }*/

        /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }*/
    }
}