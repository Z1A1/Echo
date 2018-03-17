package com.internshala.echo.activities

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.internshala.echo.R
import com.internshala.echo.activities.MainActivity.Statified.trackNotificationBuilder
import com.internshala.echo.adapters.NavigationDrawerAdapter
import com.internshala.echo.fragments.MainScreenFragment
import com.internshala.echo.fragments.SongPlayingFragment


class MainActivity : AppCompatActivity() {


    var navigationDrawerIconList: ArrayList<String> = arrayListOf()
    var imagesfornavdrawer = intArrayOf(R.drawable.navigation_allsongs, R.drawable.navigation_favorites, R.drawable.navigation_settings, R.drawable.navigation_aboutus)

    object Statified {
        var notificationManager: NotificationManager? = null

        var drawerLayout: DrawerLayout? = null
        var trackNotificationBuilder: Notification? = null

    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        navigationDrawerIconList.add("All songs")
        navigationDrawerIconList.add("Favourities")
        navigationDrawerIconList.add("Settings")
        navigationDrawerIconList.add("About Us")
        setSupportActionBar(toolbar)
        MainActivity.Statified.drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this@MainActivity, MainActivity.Statified.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        MainActivity.Statified.drawerLayout?.setDrawerListener(toggle)
        toggle.syncState()
        val mainScreenFragment = MainScreenFragment()
        this.supportFragmentManager
                .beginTransaction()
                .add(R.id.details_fragment, mainScreenFragment, "MainScreenFragment")
                .commit()
        var _navigationadapter = NavigationDrawerAdapter(navigationDrawerIconList, imagesfornavdrawer, this)
        _navigationadapter.notifyDataSetChanged()
        var navigitionRecyclerView = findViewById<RecyclerView>(R.id.navigationrecyclerview)
        navigitionRecyclerView.layoutManager = LinearLayoutManager(this)
        navigitionRecyclerView.itemAnimator = DefaultItemAnimator()
        navigitionRecyclerView.adapter = _navigationadapter
        navigitionRecyclerView.setHasFixedSize(true)
        val intent = Intent(this@MainActivity, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this@MainActivity, System.currentTimeMillis().toInt(),
                intent, 0
        )
        trackNotificationBuilder = Notification.Builder(this)
                .setContentTitle("A track is playing in background")
                .setSmallIcon(R.drawable.echo_logo)
                .setContentIntent(pIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .build()
        Statified.notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    }


    override fun onStart() {
        super.onStart()
        try {
            Statified.notificationManager?.cancel(1992)

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    override fun onStop() {
        super.onStop()
        try {
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean) {
                Statified.notificationManager?.notify(1992, trackNotificationBuilder)
            }

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    override fun onResume() {
        super.onResume()
        try {
            Statified.notificationManager?.cancel(1992)

        } catch (e: Exception) {
            e.printStackTrace()

        }

    }
}

