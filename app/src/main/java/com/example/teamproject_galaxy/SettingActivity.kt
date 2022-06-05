package com.example.teamproject_galaxy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.teamproject_galaxy.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.favButton.setOnClickListener {

        }
        binding.shareButton.setOnClickListener {

        }
    }
}