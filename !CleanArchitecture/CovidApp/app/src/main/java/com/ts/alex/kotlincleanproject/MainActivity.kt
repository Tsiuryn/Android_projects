package com.ts.alex.kotlincleanproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ts.alex.kotlincleanproject.databinding.ActivityMainBinding
import com.ts.alex.kotlincleanproject.fragments.InfoFragment
import com.ts.alex.kotlincleanproject.fragments.MapFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val vm by viewModel<MainViewModel>()
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.vToolbar
        setSupportActionBar(toolbar)

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener(listenerOfBottomNavigationView())
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                MapFragment()
            ).commit();
        }

    }

    private fun listenerOfBottomNavigationView (): BottomNavigationView.OnNavigationItemSelectedListener {
        return BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_map -> {
                    fragment = MapFragment()
                }
                R.id.nav_info -> {
                    fragment = InfoFragment()
                }
            }
            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                    .commit()
            }
            return@OnNavigationItemSelectedListener true
        }
    }
}