package com.example.movies.view

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.movies.R
import com.example.movies.util.PERMISSION_SEND_SMS
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottomNavController: NavController
    private lateinit var toolbar: androidx.appcompat.app.ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.fragment)
        navController.setGraph(R.navigation.movies_navigation)
        NavigationUI.setupActionBarWithNavController(this, navController)

        //Bottom Navigation Controller
//        bottomNavController = Navigation.findNavController(this, R.id.bottomNavView)
//        NavigationUI.setupActionBarWithNavController(this, bottomNavController)

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = bottomNavView as BottomNavigationView
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    fun checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            //OPTIONAL METHOD -> shouldShowRequestPermissionRationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)) {
                AlertDialog.Builder(this)
                    .setTitle("Send SMS permission")
                    .setMessage("This app requires access to send an SMS.")
                    .setPositiveButton("Ask me") { dialog, which ->
                        requestSmsPermission()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        notifyDetailFragment(false)
                    }
                    .show()
            } else {
                requestSmsPermission()
            }
        } else {
            notifyDetailFragment(true)
        }

    }

    private fun requestSmsPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), PERMISSION_SEND_SMS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_SEND_SMS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    notifyDetailFragment(true)
                } else {
                    notifyDetailFragment(false)
                }
            }
        }
    }

    private fun notifyDetailFragment(permissionGranted: Boolean) {
        val activeFragment = fragment.childFragmentManager.primaryNavigationFragment
        if (activeFragment is DetailFragment) {
            (activeFragment).onPermissionResult(permissionGranted)
        }
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_popular -> {
                toolbar.title = "Popular"
                navController.setGraph(R.navigation.movies_navigation)
                NavigationUI.setupActionBarWithNavController(this, navController)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_top_rated -> {
                toolbar.title = "Top Rated"
                navController.setGraph(R.navigation.top_rated_navigation)
                NavigationUI.setupActionBarWithNavController(this, navController)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favourites -> {
                toolbar.title = "Favourites"
                navController.setGraph(R.navigation.favourites_navigation)
                NavigationUI.setupActionBarWithNavController(this, navController)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}
