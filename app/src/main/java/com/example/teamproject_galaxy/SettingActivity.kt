package com.example.teamproject_galaxy

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamproject_galaxy.databinding.ActivitySettingBinding
import com.google.android.gms.maps.model.LatLng
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.friend.client.PickerClient
import com.kakao.sdk.friend.model.OpenPickerFriendRequestParams
import com.kakao.sdk.friend.model.PickerOrientation
import com.kakao.sdk.friend.model.ViewAppearance
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.link.WebSharerClient
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.template.model.*
import com.kakao.sdk.user.model.User
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

//hashkey: 2022-06-07 21:44:08.297 10447-10447/com.example.teamproject_galaxy I/KeyHash: NN7tD3z/MbOo3hIxvmAROn14uiw=

//class SettingActivity(val favStnvar: MutableMap<String, LatLng>): AppCompatActivity(){
class SettingActivity: AppCompatActivity(){

    lateinit var binding: ActivitySettingBinding
    lateinit var adapter: FavAdapter
    val favStnvar=mutableMapOf<String,LatLng>()

    val TAG="KakaoShare"

    lateinit var stnList:List<String>

    fun getAllKeys(){
        stnList=favStnvar.keys.toList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putInfo()
        getAllKeys()
        initFav()
    }

    fun putInfo(){
        favStnvar.put("인천", LatLng(37.47615415322058, 126.61683921776164))
        favStnvar.put("동인천", LatLng(37.475415909738146, 126.63262503637782))
        favStnvar.put("도원", LatLng(37.46881496146061, 126.64255417233505))
        favStnvar.put("제물포", LatLng(37.46680935971304, 126.65747357256882))
        favStnvar.put("도화", LatLng(37.466124532022356, 126.66850921181384))
    }
    fun initFav(){
        binding.favRecycler.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.favRecycler.addItemDecoration(
            DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL)
        )
        adapter= FavAdapter(stnList)
        adapter.itemClickListener=object:FavAdapter.OnItemClickListener{
            override fun OnItemClick(stnList:String,position:Int){
                var locations:LatLng?= favStnvar[adapter.stnList[position]]
                Toast.makeText(applicationContext, locations.toString(), Toast.LENGTH_SHORT).show()
                //locations.toString()= lat/lng: (37.475415909738146,126.63262503637782)
            }
        }
        binding.favRecycler.adapter=adapter
    }

    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo =
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.i("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.i("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.i("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }

//    fun shareMessage(){
//        val defaultFeed = TextTemplate(
//            text = textToShare.trimIndent(),
//            link = Link(webUrl = "https://developers.kakao.com", mobileWebUrl = "https://developers.kakao.com")
//        )
//
//        if (LinkClient.instance.isKakaoLinkAvailable(this)) {
//            // Kakao Talk sharing is available.
//            LinkClient.instance.defaultTemplate(this, defaultFeed) { linkResult, error ->
//                if (error != null) {
//                    Log.e(TAG, "Kakao Talk sharing failed.", error)
//                }
//                else if (linkResult != null) {
//                    Log.d(TAG, "Succeeded in Kakao Talk sharing. ${linkResult.intent}")
//                    startActivity(linkResult.intent)
//
//                    // If you get this message even though Kakao Talk sharing message is successfully sent, some content may not be displayed normally.
//                    Log.w(TAG, "Warning Msg: ${linkResult.warningMsg}")
//                    Log.w(TAG, "Argument Msg: ${linkResult.argumentMsg}")
//                }
//            }
//        } else {
//            // If Kakao Talk is not installed, it is recommended to share URI via web.
//            // Example of sharing URI via web
//            val sharerUrl = WebSharerClient.instance.defaultTemplateUri(defaultFeed)
//
//            // Open URI through CustomTabs.
//
//            // 1. Open URI on Chrome browser through CustomTabs.
//            try {
//                KakaoCustomTabsClient.openWithDefault(this, sharerUrl)
//            } catch(e: UnsupportedOperationException) {
//                // Exception handling if Chrome browser is not used.
//            }
//
//            // 2. Open URI on device's default browser through CustomTabs.
//            try {
//                KakaoCustomTabsClient.open(this, sharerUrl)
//            } catch (e: ActivityNotFoundException) {
//                // Exception handling if Internet browser is not used.
//            }
//        }
//    }

}