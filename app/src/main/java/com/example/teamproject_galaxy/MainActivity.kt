package com.example.teamproject_galaxy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("확인합니다","테스트입니다")
    }
}
fun main(){
    val api_key:String="74795954496a616e35354745524177"
    val RequestSubwayData:String="http://swopenapi.seoul.go.kr/api/subway/"+api_key+"/json/realtimePosition/0/5/1호선"
    val URL=URL(RequestSubwayData)
    val conn=URL.openConnection()

    print(conn)
}