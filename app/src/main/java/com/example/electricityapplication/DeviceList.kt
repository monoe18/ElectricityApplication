package com.example.electricityapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class DeviceList : AppCompatActivity() {
    lateinit var adapter : DeviceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)
        val groupname = intent.getStringExtra("groupname")
        var deviceData = DeviceData.getDeviceData()

        for(devicegroup in deviceData!!.devices){
            if(devicegroup.groupname.equals(groupname)){
                adapter = DeviceAdapter(devicegroup.devices)
            }
        }

        supportActionBar?.title =groupname


        var recyclerView : RecyclerView = findViewById(R.id.grouprecyclerview)
        recyclerView.setHasFixedSize(true)
        var layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}