package com.example.teamproject_galaxy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamproject_galaxy.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.URL

class MainActivity : AppCompatActivity() {
    val scope= CoroutineScope(Dispatchers.IO)
    lateinit var adapter:SubwayAdapter
    val api_key:String="74795954496a616e35354745524177"
    val subwayName:String="2호선"
    val RequestSubwayData:String="http://swopenapi.seoul.go.kr/api/subway/"+api_key+"/json/realtimePosition/0/999/"+subwayName
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
    }
    fun init(){
        initAdapter()
        scope.launch {
            adapter.items.clear()
            val doc= Jsoup.connect(RequestSubwayData).ignoreContentType(true).get()
            val json=JSONObject(doc.text())
            val RealTimeArray=json.getJSONArray("realtimePositionList")
            Log.i("확인",RealTimeArray.getJSONObject(1).toString())
            Log.i("확인-지하철개수",RealTimeArray.length().toString())
            for(i in 0..RealTimeArray.length()-1){
                var subwayNm=RealTimeArray.getJSONObject(i).getString("subwayNm").toString()
                var statnNm=RealTimeArray.getJSONObject(i).getString("statnNm").toString()
                var direction=RealTimeArray.getJSONObject(i).getString("updnLine").toInt()
                var LastSubway=RealTimeArray.getJSONObject(i).getString("lstcarAt").toBoolean()
                var trainSttus=RealTimeArray.getJSONObject(i).getString("trainSttus").toInt()
                var trainStatus:String
                if(trainSttus==0){
                    trainStatus="진입"
                }else if (trainSttus==1){
                    trainStatus="도착"
                }else{
                    trainStatus="출발"
                }
                adapter.items.add(Subway(subwayNm,statnNm, direction, LastSubway,trainStatus))
            }
            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initAdapter() {
        binding.recyclerView.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL)
        )
        adapter= SubwayAdapter(ArrayList<Subway>())
        adapter.itemClickListener=object:SubwayAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                Log.i("클릭",adapter.items[position].subwayNm)
                Log.i("클릭",adapter.items[position].location)
                Log.i("클릭",adapter.items[position].direction.toString())
                Log.i("클릭",adapter.items[position].LastSubway.toString())
            }
        }
        binding.recyclerView.adapter=adapter
    }
}
