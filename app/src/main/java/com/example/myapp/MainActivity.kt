package com.example.myapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.example.myapp.databinding.ActivityMainBinding

import android.content.Intent
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isLoginSuccessful = intent.getIntExtra("isLoginSuccessful", 0)

        if(isLoginSuccessful == 1){
            Log.d("MainActivity", "Navigating to fragmentAdminMenu")
            // Navigate to the desired fragment after a successful login
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.fragmentAdminMenu)
        }
        else if(isLoginSuccessful == 2){
            Log.d("MainActivity", "Navigating to clientMenuFragment")
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.clientMenuFragment)
        }else{
            Log.d("MainActivity", "Invalid value of isLoginSuccessful: $isLoginSuccessful")
        }

        val imageId = intArrayOf(
            R.drawable.ekamedica
            )

        val name = arrayOf(
            "EkaMedica Czarny bez, płyn, 1000 ml"
        )



        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "plain/text"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("PharmaShop212@gmail.com"))

            try {
                startActivity(Intent.createChooser(emailIntent, "Send email..."))
            } catch (ex: android.content.ActivityNotFoundException) {
                Snackbar.make(view, "No email app found", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}