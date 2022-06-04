package com.example.teamproject_galaxy

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamproject_galaxy.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var googleMap: GoogleMap
    lateinit var binding: ActivityMainBinding
    lateinit var adapter:SubwayAdapter
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationRequest2:LocationRequest
    lateinit var locationCallback: LocationCallback

    var stn=ArrayList<String>()
    var loc= LatLng(37.554752,126.970631)
    var subwayName:String="1호선"
    var startupdate=false
    var liveStn=ArrayList<Subway>()

    val api_key:String="74795954496a616e35354745524177"
    val scope= CoroutineScope(Dispatchers.IO)
    val RequestSubwayData:String="http://swopenapi.seoul.go.kr/api/subway/"+api_key+"/json/realtimePosition/0/999/"+subwayName
    var coordinates=ArrayList<LatLng>()
    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getLiveStn()
        Log.i("확인","1111"+liveStn.toString())
        readTextFile(44, Scanner(resources.openRawResource(R.raw.line3)))
        initmap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        initSpinner()
        //init()
    }

    private fun getLiveStn() {
        scope.launch {
            subwayName="1호선"
            val doc= Jsoup.connect(RequestSubwayData).ignoreContentType(true).get()
            val json=JSONObject(doc.text())
            val RealTimeArray=json.getJSONArray("realtimePositionList")
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
                liveStn.add(Subway(subwayNm,statnNm, direction, LastSubway,trainStatus))
            }
            withContext(Dispatchers.Main){

            }
        }
    }

    private fun initmap(markIcon: BitmapDescriptor){  //coordinates:ArrayList<LatLng>

        //initLocation()
        val mapFragment=supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync{
            //googleMap.clear()
            googleMap=it
            //initLocation()
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates[0],16.0f))
            googleMap.setMinZoomPreference(10.0f)
            googleMap.setMaxZoomPreference(18.0f)

            //Marker:
            for(cor in coordinates){
                if(cor!=null) {
                    val option = MarkerOptions()
                    option.position(cor)
                    option.icon(
                        markIcon
                    )

                    var stnName = coordinates.indexOf(cor)
                    option.title(stn[stnName])
                    //option2.snippet("서울역")
                    googleMap.addMarker(option)?.showInfoWindow()

                }
            }
        }
    }


    private fun readTextFile(counts:Int,scans:Scanner){
        var dones:Boolean=true
        var counting=0
        val scan= scans
        while(dones){
            val newline: String = scan.nextLine()
            val lines = newline
            val info = lines.split(", ","-")
            val double1: Double = info[0].toDouble()
            val double2: Double = info[1].toDouble()
            val locate = LatLng(double1, double2)
            coordinates.add(locate)
            stn.add(info[2])
            counting++
            if(counting==counts)break
        }
    }
    fun init(){
        //initAdapter()
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
    private fun initSpinner() {
        val adapter= ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_dropdown_item,ArrayList<String>())

        adapter.add("1호선")
        adapter.add("2호선")
        adapter.add("3호선")
        adapter.add("4호선")
        adapter.add("5호선")
        adapter.add("6호선")
        adapter.add("7호선")
        adapter.add("8호선")
        adapter.add("9호선")
        binding.apply {
            spinner.adapter=adapter
            spinner.setSelection(1)
            spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    when(position){
                        0->{
                            stn.clear()
                            googleMap.clear()
                            coordinates.clear()
                            val scanText= Scanner(resources.openRawResource(R.raw.line1))
                            readTextFile(99,scanText)
                            initmap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        }
                        1->{
                            stn.clear()
                            coordinates.clear()
                            googleMap.clear()
                            val scanText= Scanner(resources.openRawResource(R.raw.line2))
                            readTextFile(51,scanText)
                            initmap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        }
                        2->{
                            stn.clear()
                            coordinates.clear()
                            googleMap.clear()
                            val scanText= Scanner(resources.openRawResource(R.raw.line3))
                            readTextFile(44,scanText)
                            initmap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        }
                        3->{
                            stn.clear()
                            coordinates.clear()
                            googleMap.clear()
                            val scanText= Scanner(resources.openRawResource(R.raw.line4))
                            readTextFile(50,scanText)
                            initmap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        }
                        4->{
                            stn.clear()
                            coordinates.clear()
                            googleMap.clear()
                            val scanText= Scanner(resources.openRawResource(R.raw.line5))
                            readTextFile(49,scanText)
                            initmap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                        }
                        5->{
                            stn.clear()
                            coordinates.clear()
                            googleMap.clear()
                            val scanText= Scanner(resources.openRawResource(R.raw.line6))
                            readTextFile(39,scanText)
                            initmap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                        }
                        6->{
                            stn.clear()
                            coordinates.clear()
                            googleMap.clear()

                            val scanText= Scanner(resources.openRawResource(R.raw.line7))
                            readTextFile(53,scanText)
                            initmap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                        }
                        7->{
                            stn.clear()
                            coordinates.clear()
                            googleMap.clear()
                            val scanText= Scanner(resources.openRawResource(R.raw.line8))
                            readTextFile(18,scanText)
                            initmap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                        }
                        8->{
                            stn.clear()
                            coordinates.clear()
                            googleMap.clear()
                            val scanText= Scanner(resources.openRawResource(R.raw.line9))
                            readTextFile(38,scanText)
                            initmap(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        }
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }

    /*private fun initAdapter() {
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
    }*/
}
