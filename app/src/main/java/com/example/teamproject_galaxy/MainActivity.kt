package com.example.teamproject_galaxy

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
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
import com.google.android.gms.maps.model.*
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

    var MAPS_API_KEY="AIzaSyD_HxDVhJrotISF17sQoEPpL-sN0TOXNqY"
    var stn=ArrayList<String>()
    var coordinates=ArrayList<LatLng>()
    var setMarker=false
    var loc= LatLng(37.554752,126.970631)

    var subwayName:String="1호선"
    var startupdate=false
    var liveStn=ArrayList<Subway>()

    val api_key:String="74795954496a616e35354745524177"
    val scope= CoroutineScope(Dispatchers.IO)
    val RequestSubwayData:String="http://swopenapi.seoul.go.kr/api/subway/"+api_key+"/json/realtimePosition/0/999/"+subwayName

    var coordinates1=ArrayList<LatLng>()
    var stn1=ArrayList<String>()
    var coordinates2=ArrayList<LatLng>()
    var stn2=ArrayList<String>()
    var coordinates3=ArrayList<LatLng>()
    var stn3=ArrayList<String>()
    var coordinates4=ArrayList<LatLng>()
    var stn4=ArrayList<String>()
    var coordinates5=ArrayList<LatLng>()
    var stn5=ArrayList<String>()
    var coordinates6=ArrayList<LatLng>()
    var stn6=ArrayList<String>()
    var coordinates7=ArrayList<LatLng>()
    var stn7=ArrayList<String>()
    var coordinates8=ArrayList<LatLng>()
    var stn8=ArrayList<String>()
    var coordinates9=ArrayList<LatLng>()
    var stn9=ArrayList<String>()

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

        initLayout()
        saveArray()
        initmap(BitmapDescriptorFactory.HUE_GREEN)
        initSpinner()
        //init()
    }

    private fun initLayout() {
        binding.button.setOnClickListener {
            val intent= Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveArray(){
        val scanText1= Scanner(resources.openRawResource(R.raw.line1))
        readTextFile(99,scanText1,coordinates1,stn1)
        val scanText2= Scanner(resources.openRawResource(R.raw.line2))
        readTextFile(51,scanText2,coordinates2,stn2)
        val scanText3= Scanner(resources.openRawResource(R.raw.line3))
        readTextFile(44,scanText3,coordinates3,stn3)
        val scanText4= Scanner(resources.openRawResource(R.raw.line4))
        readTextFile(50,scanText4,coordinates4,stn4)
        val scanText5= Scanner(resources.openRawResource(R.raw.line5))
        readTextFile(56,scanText5,coordinates5,stn5)
        val scanText6= Scanner(resources.openRawResource(R.raw.line6))
        readTextFile(39,scanText6,coordinates6,stn6)
        val scanText7= Scanner(resources.openRawResource(R.raw.line7))
        readTextFile(53,scanText7,coordinates7,stn7)
        val scanText8= Scanner(resources.openRawResource(R.raw.line8))
        readTextFile(18,scanText8,coordinates8,stn8)
        val scanText9= Scanner(resources.openRawResource(R.raw.line9))
        readTextFile(38,scanText9,coordinates9,stn9)
    }

    private fun getLiveStn() {
        scope.launch {
            subwayName="1호선"
            val doc= Jsoup.connect(RequestSubwayData).ignoreContentType(true).get()
            val json=JSONObject(doc.text())
            Log.i("확인",json.toString());
            if(json.getJSONObject("errorMessage").getInt("status")==200) {
                val RealTimeArray = json.getJSONArray("realtimePositionList")
                for (i in 0..RealTimeArray.length() - 1) {
                    var subwayNm = RealTimeArray.getJSONObject(i).getString("subwayNm").toString()
                    var statnNm = RealTimeArray.getJSONObject(i).getString("statnNm").toString()
                    var direction = RealTimeArray.getJSONObject(i).getString("updnLine").toInt()
                    var LastSubway =
                        RealTimeArray.getJSONObject(i).getString("lstcarAt").toBoolean()
                    var trainSttus = RealTimeArray.getJSONObject(i).getString("trainSttus").toInt()
                    var trainStatus: String
                    if (trainSttus == 0) {
                        trainStatus = "진입"
                    } else if (trainSttus == 1) {
                        trainStatus = "도착"
                    } else {
                        trainStatus = "출발"
                    }
                    liveStn.add(Subway(subwayNm, statnNm, direction, LastSubway, trainStatus))
                }
            }
            Log.i("확인",liveStn.toString())
            withContext(Dispatchers.Main){

            }
        }
    }

    private fun initmap(colour:Float){  //coordinates:ArrayList<LatLng>

        val mapFragment=supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync{
            googleMap=it
            googleMap.setMinZoomPreference(10.0f)
            googleMap.setMaxZoomPreference(18.0f)

            //Marker:
            if(setMarker) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates[0],16.0f))
                for (cor in coordinates) {
                    val option = MarkerOptions()
                    option.position(cor)
                    option.icon(
                        BitmapDescriptorFactory.defaultMarker(colour)
                    )
                    var stnName = coordinates.indexOf(cor)
                    option.title(stn[stnName])
                    //option2.snippet("서울역")
                    googleMap.addMarker(option)?.showInfoWindow()
                }
                val option3 = PolylineOptions().color(Color.DKGRAY).addAll(coordinates)
                googleMap.addPolyline(option3)
            }else
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,16.0f))
        }
    }

    private fun readTextFile(counts:Int,scans:Scanner,locArray:ArrayList<LatLng>,stnArray:ArrayList<String>){
        var dones:Boolean=true
        var counting=1
        while(dones){
            val newline: String = scans.nextLine()
            val info = newline.split(", ","-")
            val double1: Double = info[0].toDouble()
            val double2: Double = info[1].toDouble()
            val locate = LatLng(double1, double2)
            locArray.add(locate)
            stnArray.add(info[2])
            if(counting==counts)dones=false
            else counting++
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
        val adapter=ArrayAdapter<String>(this,
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
            //spinner.setSelection(1)
            spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    setMarker=true
                    googleMap.clear()
                    coordinates.clear()
                    stn.clear()
                    when(position){
                        0->{
                            coordinates=coordinates1
                            stn=stn1
                            initmap(BitmapDescriptorFactory.HUE_BLUE)
                        }
                        1->{
                            coordinates=coordinates2
                            stn=stn2
                            initmap(BitmapDescriptorFactory.HUE_GREEN)
                        }
                        2->{
                            coordinates=coordinates3
                            stn=stn3
                            initmap(BitmapDescriptorFactory.HUE_ORANGE)
                        }
                        3->{
                            coordinates=coordinates4
                            stn=stn4
                            initmap(BitmapDescriptorFactory.HUE_CYAN)
                        }
                        4->{
                            coordinates=coordinates5
                            stn=stn5
                            initmap(BitmapDescriptorFactory.HUE_VIOLET)
                        }
                        5->{
                            coordinates=coordinates6
                            stn=stn6
                            initmap(BitmapDescriptorFactory.HUE_YELLOW)
                        }
                        6->{
                            coordinates=coordinates7
                            stn=stn7
                            initmap(BitmapDescriptorFactory.HUE_MAGENTA)
                        }
                        7->{
                            coordinates=coordinates8
                            stn=stn8
                            initmap(BitmapDescriptorFactory.HUE_ROSE)
                        }
                        8->{
                            coordinates=coordinates9
                            stn=stn9
                            initmap(BitmapDescriptorFactory.HUE_RED)
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
