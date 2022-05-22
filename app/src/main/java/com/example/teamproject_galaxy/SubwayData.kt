package com.example.teamproject_galaxy

import java.net.URL

class SubwayData {
}

fun main(){
    val api_key:String="74795954496a616e35354745524177"
    val RequestSubwayData:String="http://swopenapi.seoul.go.kr/api/subway/"+api_key+"/json/realtimePosition/0/5/1호선"
    val URL= URL(RequestSubwayData)
    val conn=URL.openConnection()

    print(conn)
}