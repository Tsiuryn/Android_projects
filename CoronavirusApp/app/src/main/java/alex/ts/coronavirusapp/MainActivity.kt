package alex.ts.coronavirusapp

import alex.ts.coronavirusapp.view.InfoFragment
import alex.ts.coronavirusapp.view.MapFragment
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(listenerOfBottomNavigationView())
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                MapFragment()
            ).commit();
        }
    }

    private fun listenerOfBottomNavigationView (): BottomNavigationView.OnNavigationItemSelectedListener {
        val navListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
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
        return navListener
    }
}
