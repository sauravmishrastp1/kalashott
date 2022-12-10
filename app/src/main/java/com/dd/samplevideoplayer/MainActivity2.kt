package com.dd.samplevideoplayer

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.db.ChangeOrintation
import com.dd.api.Constants.Companion.isRotate
import com.dd.api.baseModel.Data_
import com.dd.samplevideoplayer.databinding.ActivityMain2Binding
import com.dd.samplevideoplayer.databinding.LayoutBannerBinding
import com.dd.samplevideoplayer.ui.ShareViewModel
import com.dd.samplevideoplayer.ui.home.adapter.BannerAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity2 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bannerAdapter: BannerAdapter
    private val shareViewModel:ShareViewModel by viewModels()

    companion object {
        lateinit var binding: ActivityMain2Binding
//        var l: ChangeOrintation?=null

    }

    private lateinit var adapterLayoutBannerBinding: LayoutBannerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        bannerAdapter = BannerAdapter(object : BannerAdapter.BannerAdapterCallBack {
            override fun sendData(
                data: ArrayList<Data_>,
                position: Int,
                type: String,
                binding: LayoutBannerBinding
            ) {
                adapterLayoutBannerBinding = binding


            }

        })

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.appBarMain.imgSearch.visibility = View.VISIBLE

            when (destination.id) {

                R.id.searchFragment -> {

                    binding.appBarMain.imgSearch.visibility = View.GONE

                }R.id.navigation_about->{
                binding.appBarMain.imgSearch.visibility = View.GONE

            }

                else -> {
                    binding.appBarMain.imgSearch.visibility = View.VISIBLE


                }
            }
        }
        binding.appBarMain.imgSearch.setOnClickListener {
            navController.navigate(R.id.searchFragment)
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_notifications,
                R.id.navigation_my_list,
                R.id.navigation_about
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }




}