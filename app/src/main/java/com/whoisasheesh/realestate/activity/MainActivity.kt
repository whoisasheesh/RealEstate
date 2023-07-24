package com.whoisasheesh.realestate.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.whoisasheesh.realestate.R
import com.whoisasheesh.realestate.fragment.PropertyListingFragment
import com.whoisasheesh.realestate.listeners.ScreenChangeListener

class MainActivity : AppCompatActivity(), ScreenChangeListener {
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PropertyListingFragment())
                .commit()
        }
    }

    override fun setupCustomToolbarWithoutNavigation(
        title: String,
        icon: Int,
        isNavigationIconEnabled: Boolean
    ) {
        supportActionBar?.let {
            this@MainActivity.findViewByIdIfExists<TextView>(R.id.toolbar_text_title)?.text = title

            if (isNavigationIconEnabled) {
                findViewByIdIfExists<ImageView>(R.id.toolbar_icon)?.apply {
                    visibility = View.VISIBLE
                    setImageResource(icon)
                    setOnClickListener {
                        supportFragmentManager.popBackStackImmediate()
                    }
                }
            } else {
                findViewByIdIfExists<ImageView>(R.id.toolbar_icon)?.apply {
                    visibility = View.GONE
                }
            }

            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    private fun <T : View> Activity.findViewByIdIfExists(id: Int): T? {
        return try {
            findViewById(id)
        } catch (exception: NullPointerException) {
            null
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment is PropertyListingFragment) {
            // Show the exit confirmation dialog
            currentFragment.showExitConfirmationDialog()
        } else {
            super.onBackPressed()
        }
    }


}