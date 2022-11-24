package com.example.electricityapplication

import android.graphics.Color
import android.graphics.Paint
import android.graphics.SweepGradient
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.electricityapplication.networking.RecordMetadata
import com.example.electricityapplication.networking.RecordService
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.HTTP
import java.lang.Thread.sleep
import java.time.LocalDate
import java.util.Collections
import java.util.Date
import kotlin.math.log


class MainActivity : AppCompatActivity() {
    private var jsonData : RecordMetadata? = null
    lateinit var chart: BarChart
    lateinit var barData: BarData
    lateinit var barDataSet: BarDataSet
    lateinit var barEntriesList: ArrayList<BarEntry>
    var doneGetting = false
    var logging : HttpLoggingInterceptor = HttpLoggingInterceptor()
    var deviceData :DeviceData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        var client : OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()
        var retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://api.energidataservice.dk")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        var jsonAPI : RecordService = retrofit.create(RecordService::class.java)

        var startDate = LocalDate.now().minusDays(1).toString() + "T00:00"
        var endDate = LocalDate.now().toString() + "T00:00"
        Log.i("netlog",startDate.toString())
        Log.i("netlog",endDate.toString())
        var call : Call<RecordMetadata> = jsonAPI.getRecords(startDate = startDate, endDate = endDate)

        call.enqueue(object : Callback<RecordMetadata> {
            override fun onResponse(
                call: Call<RecordMetadata>,
                response: Response<RecordMetadata>
            ) {
                //check if successful and save data
                if(!response.isSuccessful){
                    Log.i("netlog", "Code: " + response.code())
                    return
                }
                Log.i("netlog", response.body().toString())
                jsonData = response.body();
                runOnUiThread {
                    setupBarChart()
                }



            }
            override fun onFailure(call: Call<RecordMetadata>, t: Throwable) {
                Log.i("netlog", t.message.toString())
            }
        })
        var pricebox : TextView = findViewById(R.id.currentPrice)
        var usage : TextView = findViewById(R.id.currentUsage)

        deviceData = DeviceData.getDeviceData()
        var totalcost = 0.0
        var totalusage = 0.0
        for(devicegroup in deviceData!!.devices){
            var grouptotalcost = 0.0
            var grouptotalusage = 0.0
            for (device in devicegroup.devices){
                if(device.cost!= null){
                    grouptotalcost += device.cost!!
                }
                grouptotalusage+= device.usage
            }
            totalcost += grouptotalcost
            totalusage+= grouptotalusage
        }
        pricebox.text = "%.2f".format(totalcost) + " kr."
        usage.text = "%.2f".format(totalusage) + " kWh"

        var recyclerView : RecyclerView = findViewById(R.id.mainrecyclerview)
        var adapter = MainAdapter(deviceData!!.devices,this)
        recyclerView.setHasFixedSize(true)
        var layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

    }

    fun setupBarChart(){
        chart = findViewById(R.id.chart1)
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.getDescription().setEnabled(false)
        chart.setMaxVisibleValueCount(60)
        chart.setPinchZoom(false)
        chart.setTouchEnabled(false)
        chart.setDrawGridBackground(false)
        setData()
    }

    fun setData(){
        val quarters = arrayOf(
            "00", "01", "02", "03", "04", "05", "06",
            "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20", "21", "22", "23",
        )
        barEntriesList = arrayListOf()
        getData()
        barDataSet = BarDataSet(barEntriesList, "Bar Chart Data")
        barData = BarData(barDataSet)
        Log.i("netlog", barDataSet.toString())
        chart.data = barData
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.setColor(resources.getColor(R.color.purple_200))
        barDataSet.valueTextSize = 10f
        barDataSet.setDrawValues(false)
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisRight.isEnabled =false
        chart.axisRight.setDrawGridLines(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.axisMinimum = 0f
        var myValueFormatter: ValueFormatter = object: ValueFormatter(){
            override fun getAxisLabel(value: Float, axis : AxisBase): String{
                return quarters[value.toInt()]
            }
        }
        /*val startColor1 = ContextCompat.getColor(this, )
        val endColor1 = ContextCompat.getColor(this, android.R.color.holo_green_light)
        val mPaint: Paint =
            chart.renderer.paintRender
        mPaint.setShader(SweepGradient(
                350,
                120,
                Color.,
                Color.parseColor("#FCE121")
            ))*/

        chart.xAxis.granularity = 1f
        chart.xAxis.valueFormatter = myValueFormatter
        barDataSet.setGradientColor(Color.parseColor("#699a00"),Color.parseColor("#ADD095"))
        chart.xAxis.setLabelCount(23, true)
        chart.xAxis.labelRotationAngle = -45f
        Log.i("netlog", "Reached notifyDataSet")
        chart.notifyDataSetChanged()
        chart.invalidate()
        chart.refreshDrawableState()
    }

    fun getData(){
        var i = 0
        Log.i("netlog", "Reached GetData")
        Log.i("netlog", jsonData!!.records.size.toString())
        for (record in jsonData!!.records){
            Log.i("netlog", "got a line of records: " + record.SpotPriceDKK.toString())
            barEntriesList.add(BarEntry(i.toFloat(),(record.SpotPriceDKK/1000).toFloat()))
            i++
        }
        Log.i("netlog", barEntriesList.toString())
    /*
        barEntriesList.add(BarEntry(0.0f,0.6f))
        barEntriesList.add(BarEntry(1.0f,0.65f))
        barEntriesList.add(BarEntry(2.0f,0.7f))
        barEntriesList.add(BarEntry(3.0f,0.73f))
        barEntriesList.add(BarEntry(4.0f,0.8f))
        barEntriesList.add(BarEntry(5.0f,0.6f))

        barEntriesList.add(BarEntry(6.0f,0.7f))
        barEntriesList.add(BarEntry(7.0f,0.9f))
        barEntriesList.add(BarEntry(8.0f,1.0f))
        barEntriesList.add(BarEntry(9.0f,1.03f))
        barEntriesList.add(BarEntry(10.0f,1.05f))

        barEntriesList.add(BarEntry(11.0f,1.1f))
        barEntriesList.add(BarEntry(12.0f,1.13f))
        barEntriesList.add(BarEntry(13.0f,1.19f))
        barEntriesList.add(BarEntry(14.0f,1.2f))
        barEntriesList.add(BarEntry(15.0f,1.21f))


        barEntriesList.add(BarEntry(16.0f,1.23f))
        barEntriesList.add(BarEntry(17.0f,1.29f))
        barEntriesList.add(BarEntry(18.0f,1.2f))
        barEntriesList.add(BarEntry(19.0f,1.17f))
        barEntriesList.add(BarEntry(20.0f,1.12f))

        barEntriesList.add(BarEntry(21.0f,1.0f))
        barEntriesList.add(BarEntry(22.0f,0.95f))
        barEntriesList.add(BarEntry(23.0f,0.92f))*/
    }

}