package com.example.teamproject_galaxy

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.teamproject_galaxy.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.*
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.PrintStream
import java.util.*
import java.util.jar.Attributes
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var googleMap: GoogleMap
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: SubwayAdapter
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
    var stn_location= mutableMapOf<String,LatLng>()
    var liveStn=ArrayList<Subway>()

    val api_key:String="74795954496a616e35354745524177"
    val scope= CoroutineScope(Dispatchers.IO)
    var RequestSubwayData:String="http://swopenapi.seoul.go.kr/api/subway/"+api_key+"/json/realtimePosition/0/999/"+subwayName
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
    var sub_list=ArrayList<Marker>()
    var like_stnMap= mutableMapOf<String,Int>()
    val colour= listOf<Float>(BitmapDescriptorFactory.HUE_BLUE
        ,BitmapDescriptorFactory.HUE_GREEN
        ,BitmapDescriptorFactory.HUE_ORANGE
        ,BitmapDescriptorFactory.HUE_CYAN
        ,BitmapDescriptorFactory.HUE_VIOLET
        ,BitmapDescriptorFactory.HUE_YELLOW
        ,BitmapDescriptorFactory.HUE_MAGENTA
        ,BitmapDescriptorFactory.HUE_ROSE
        ,BitmapDescriptorFactory.HUE_RED
    )
    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getLiveStn()
       initLayout()
        saveArray()
        getLikeList()
        initmap(BitmapDescriptorFactory.HUE_GREEN)
        initSpinner()
        //init()
    }

    private fun getLikeList() {
        try {
            var stnlike:String
            for(i in stn_location.keys) {
                val scan = Scanner(openFileInput(i+".txt"))
                while (scan.hasNextLine()) {
                    stnlike=scan.nextLine()
                    like_stnMap[i]=stnlike.toInt()
                }
            }
        }catch (e:Exception){//없을경우 파일 생성
            stn_location
            for(i in stn_location.keys) {
                val output = PrintStream(openFileOutput(i+".txt", Context.MODE_PRIVATE))
                output.println(0)
                output.close()
            }
        }
    }

    fun share(){

    }
    fun write_likeSubway(subway:String,like:Int){
        val output = PrintStream(openFileOutput(subway+".txt", Context.MODE_PRIVATE))
        output.println(like)
        output.close()
    }
    private fun initLayout() {
        binding.share.setOnClickListener {
            share()//공유기능
        }
        binding.like.setOnClickListener {
            val subname=binding.titleSubway.text.toString()
            if(like_stnMap[subname]==0){
                like_stnMap[subname]=1
                write_likeSubway(subname,1)
                binding.like.setColorFilter(Color.parseColor("#FFFF00"))
            }else{
                like_stnMap[subname]=0
                write_likeSubway(subname,0)
                binding.like.setColorFilter(Color.parseColor("#000000"))
            }
        }
        binding.cardView.visibility=View.GONE
        binding.cardView.bringToFront()
        binding.cardView.setOnClickListener {
        }
        binding.button.setOnClickListener {
            val intent= Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        binding.imageView.setOnClickListener{
            setMarker=true
            googleMap.clear()
            val num=subwayName.slice(0..0).toInt()-1
            Log.i("확인",num.toString())
            when(num){
                0->{
                    subwayName="1호선"
                    initmap(colour[num])
                }
                1->{
                    subwayName="2호선"
                    initmap(colour[num])
                }
                2->{
                    subwayName="3호선"
                    initmap(colour[num])
                }
                3->{
                    subwayName="4호선"
                    initmap(colour[num])
                }
                4->{
                    subwayName="5호선"
                    initmap(colour[num])
                }
                5->{
                    subwayName="6호선"
                    initmap(colour[num])
                }
                6->{
                    subwayName="7호선"
                    initmap(colour[num])
                }
                7->{
                    subwayName="8호선"
                    initmap(colour[num])
                }
                8->{
                    subwayName="9호선"
                    initmap(colour[num])
                }
            }
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
        readTextFile(51,scanText4,coordinates4,stn4)
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
        RequestSubwayData="http://swopenapi.seoul.go.kr/api/subway/"+api_key+"/json/realtimePosition/0/999/"+subwayName
        val Job=scope.launch {
            var subarray = ArrayList<Subway>()
            val doc = Jsoup.connect(RequestSubwayData).ignoreContentType(true).get()
            val json = JSONObject(doc.text())
            //Log.i("확인", json.toString());
            if (json.getJSONObject("errorMessage").getInt("status") == 200) {
                val RealTimeArray = json.getJSONArray("realtimePositionList")
                //Log.i("확인",RealTimeArray.length().toString())
                for (i in 0..RealTimeArray.length() - 1) {
                    var subwayNm =RealTimeArray.getJSONObject(i).getString("subwayNm").toString()
                    var statnNm = RealTimeArray.getJSONObject(i).getString("statnNm").toString()
                    var direction = RealTimeArray.getJSONObject(i).getString("updnLine").toInt()
                    var LastSubway =
                        RealTimeArray.getJSONObject(i).getString("lstcarAt").toBoolean()
                    var trainSttus =
                        RealTimeArray.getJSONObject(i).getString("trainSttus").toInt()
                    var trainStatus: String
                    if (trainSttus == 0) {
                        trainStatus = "진입"
                    } else if (trainSttus == 1) {
                        trainStatus = "도착"
                    } else {
                        trainStatus = "출발"
                    }
                    subarray.add(Subway(subwayNm, statnNm, direction, LastSubway, trainStatus))
                }
            }
            withContext(Dispatchers.Default) {
                liveStn = subarray
            }
        }
        runBlocking {
            Job.join()
        }
    }

    private fun initmap(colour:Float){  //coordinates:ArrayList<LatLng>
        getLiveStn()
        sub_list.clear()
        val imageBitmap:Bitmap= BitmapFactory.decodeResource(resources,resources.getIdentifier("subway","drawable",packageName))
        val BitmapIcon=Bitmap.createScaledBitmap(imageBitmap,100,100,false)
        val mapFragment=supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            googleMap.setMinZoomPreference(10.0f)
            googleMap.setMaxZoomPreference(18.0f)
            //Marker:
            if (setMarker) {

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates[0], 16.0f))
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
                for (subway in liveStn) {
                    val sub_option = MarkerOptions()
                    val position = stn_location.getValue(subway.location)
                    sub_option.position(position)
                    sub_option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    sub_option.title("실시간 위치")
                    sub_option.snippet(subway.location)
                    sub_option.icon(BitmapDescriptorFactory.fromBitmap(BitmapIcon))
                    val marker=googleMap.addMarker(sub_option) as Marker
                    marker?.showInfoWindow()
                    sub_list.add(marker)
                }
                when (subwayName) {
                    // 1호선
                    "1호선" -> {
                        val polylineOptions = PolylineOptions().color(Color.BLUE)
                        for (i in 0..24) {
                            polylineOptions.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions)
                        polylineOptions.add(coordinates[23])
                        for (i in 25..38) {
                            polylineOptions.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions)
                        polylineOptions.add(coordinates[37])
                        for (i in 39..57) {
                            polylineOptions.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions)
                        val polylineOptions2 = PolylineOptions().color(Color.BLUE)
                        polylineOptions2.add(coordinates[20])
                        for (i in 58..98) {
                            polylineOptions2.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions2)
                    }
                    // 2호선
                    "2호선" -> {
                        val polylineOptions = PolylineOptions().color(Color.GREEN)
                        for (i in 0..4) {
                            polylineOptions.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions)
                        val polylineOptions2 = PolylineOptions().color(Color.GREEN)
                        for (i in 27..31) {
                            polylineOptions2.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions2)
                        val polygonOptions = PolygonOptions().strokeColor(Color.GREEN)
                        for (i in 4..27) {
                            polygonOptions.add(coordinates[i])
                        }
                        for (i in 32..50) {
                            polygonOptions.add(coordinates[i])
                        }
                        googleMap.addPolygon(polygonOptions)
                    }
                    // 3호선
                    "3호선" -> {
                        val polylineOptions = PolylineOptions().color(Color.rgb(255, 165, 0))
                        for (cor in coordinates) {
                            polylineOptions.add(cor)
                        }
                        googleMap.addPolyline(polylineOptions)
                    }
                    // 4호선
                    "4호선" -> {
                        val polylineOptions = PolylineOptions().color(Color.CYAN)
                        for (cor in coordinates) {
                            polylineOptions.add(cor)
                        }
                        googleMap.addPolyline(polylineOptions)
                    }
                    // 5호선
                    "5호선" -> {
                        val polylineOptions = PolylineOptions().color(Color.rgb(138, 43, 226))
                        for (i in 0..48) {
                            polylineOptions.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions)
                        val polylineOptions2 = PolylineOptions().color(Color.rgb(138, 43, 226))
                        polylineOptions2.add(coordinates[38]) //38
                        for (i in 49..55) {
                            polylineOptions2.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions2)
                    }
                    // 6호선
                    "6호선" -> {
                        val polygonOptions = PolygonOptions().strokeColor(Color.rgb(205, 124, 47))
                        for (i in 0..5) {
                            polygonOptions.add(coordinates[i])
                        }
                        googleMap.addPolygon(polygonOptions)
                        val polylineOptions = PolylineOptions().color(Color.rgb(205, 124, 47))
                        for (i in 5..38) {  //5~38
                            polylineOptions.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions)
                    }
                    // 7호선
                    "7호선" -> {
                        val polylineOptions = PolylineOptions().color(Color.YELLOW)
                        for (cor in coordinates) {
                            polylineOptions.add(cor)
                        }
                        val polyline = googleMap.addPolyline(polylineOptions)
                       // googleMap.addPolyline(polylineOptions)
                    }
                    // 8호선
                    "8호선" -> {
                        val polylineOptions = PolylineOptions().color(Color.rgb(255, 0, 127))
                        for (cor in coordinates) {
                            polylineOptions.add(cor)
                        }
                        val polyline = googleMap.addPolyline(polylineOptions)
                    }
                    // 9호선
                    "9호선" -> {
                        val polylineOptions = PolylineOptions().color(Color.RED)
                        for (cor in coordinates) {
                            polylineOptions.add(cor)
                        }
                        val polyline = googleMap.addPolyline(polylineOptions)
                    }
                }
            } else
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f))

            googleMap.setOnMarkerClickListener(object:GoogleMap.OnMarkerClickListener{
                override fun onMarkerClick(p0: Marker): Boolean {
                    if(p0.title!="실시간 위치") {//지하철일경우 이벤트 처리
                        binding.titleSubway.text=p0.title
                        binding.cardView.visibility=View.VISIBLE
                        if(like_stnMap[p0.title]==1){
                            binding.like.setColorFilter(Color.parseColor("#FFFF00"))
                        }else{
                            binding.like.setColorFilter(Color.parseColor("#000000"))
                        }
                        val NearestMarker=getNearestMarker(p0)
                        binding.NearestSubName.text=NearestMarker.snippet
                        binding.NearestSubName.setOnClickListener {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(NearestMarker.position,16.0f))
                        }
                    }

                    return false
                }
            })
            googleMap.setOnMapClickListener(object:GoogleMap.OnMapClickListener{
                override fun onMapClick(p0: LatLng) {
                    binding.cardView.visibility=View.GONE
                }
            })
        }
    }

    private fun getNearestMarker(p0: Marker): Marker {
        val x=p0.position.latitude
        val y=p0.position.longitude
        var distance:Double=999999.0
        var cal:Double
        var nearestMarker:Marker=p0
        for(i in sub_list){
            cal=(i.position.latitude-x)*(i.position.latitude-x)+(i.position.longitude-y)*(i.position.longitude-y)
            if(cal<distance&&i.snippet!=p0.title){
                nearestMarker=i
                distance=cal
            }
        }
        return nearestMarker

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
            stn_location.put(info[2],locate)
            like_stnMap.put(info[2],0)
            stnArray.add(info[2])
            if(counting==counts)dones=false
            else counting++
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
                            subwayName="1호선"
                            coordinates=coordinates1
                            stn=stn1
                            initmap(colour[position])
                        }
                        1->{
                            subwayName="2호선"
                            coordinates=coordinates2
                            stn=stn2
                            initmap(colour[position])
                        }
                        2->{
                            subwayName="3호선"
                            coordinates=coordinates3
                            stn=stn3
                            initmap(colour[position])
                        }
                        3->{
                            subwayName="4호선"
                            coordinates=coordinates4
                            stn=stn4
                            initmap(colour[position])
                        }
                        4->{
                            subwayName="5호선"
                            coordinates=coordinates5
                            stn=stn5
                            initmap(colour[position])
                        }
                        5->{
                            subwayName="6호선"
                            coordinates=coordinates6
                            stn=stn6
                            initmap(colour[position])
                        }
                        6->{
                            subwayName="7호선"
                            coordinates=coordinates7
                            stn=stn7
                            initmap(colour[position])
                        }
                        7->{
                            subwayName="8호선"
                            coordinates=coordinates8
                            stn=stn8
                            initmap(colour[position])
                        }
                        8->{
                            subwayName="9호선"
                            coordinates=coordinates9
                            stn=stn9
                            initmap(colour[position])
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