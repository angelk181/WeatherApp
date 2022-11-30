package com.example.weatherapp.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.network.NetworkHelper
import com.example.weatherapp.databinding.ActivitySplashBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor =
            ContextCompat.getColor(this, R.color.red)


        val generalLayout = findViewById<LinearLayout>(R.id.generalLayout)
        lifecycleScope.launch {
            if (NetworkHelper.isNetworkConnected(this@SplashActivity)) {
                binding.appIcon.alpha = 0f
                binding.appIcon.animate().setDuration(1500).alpha(1f).withEndAction {


                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    finish()
                }
            } else {
                    Snackbar.make(generalLayout,getString(R.string.no_network_msg),Snackbar.LENGTH_INDEFINITE).show()
                }

            }
        }
    }