package com.example.teamproject_galaxy

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log

import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.teamproject_galaxy.databinding.HomepageBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class HomeActivity : AppCompatActivity() {
    lateinit var binding:HomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getHashKey()
        initLayout()
    }

    private fun initLayout(){
        val start = findViewById<Button>(R.id.startbtn)

        start.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.i("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.i("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.i("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }



}