package com.dd.samplevideoplayer

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dd.samplevideoplayer.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.menuClose.visibility = View.GONE
            binding.menuOpen.visibility = View.VISIBLE
            when (destination.id) {

                R.id.videoPlayFragment -> {
                    binding.navView.visibility = View.GONE
                    binding.constraintLayout.visibility = View.GONE
                }
                else -> {
                    binding.navView.visibility = View.VISIBLE
                    binding.navView.visibility = View.GONE
                    binding.constraintLayout.visibility = View.VISIBLE

                }
            }
        }
        binding.menuOpen.setOnClickListener {
            binding.navView.visibility = View.VISIBLE
            binding.menuClose.visibility = View.VISIBLE
            binding.menuOpen.visibility = View.GONE
        }

        binding.menuClose.setOnClickListener {
            binding.navView.visibility = View.GONE
            binding.menuClose.visibility = View.GONE
            binding.menuOpen.visibility = View.VISIBLE
        }
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        }
    }
}

