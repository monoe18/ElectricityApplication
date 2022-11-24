package com.example.electricityapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class DeviceAdapter(private val data : ArrayList<Device>) : RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    inner class ViewHolder(item : View): RecyclerView.ViewHolder(item){
        val deviceName : TextView = item.findViewById(R.id.name)
        val usage : TextView = item.findViewById(R.id.usage)
        val price : TextView = item.findViewById(R.id.cost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.deviceName.text = data[position].name
        holder.price.text = "%.2f".format(data[position].cost) + "DKK"
        holder.usage.text = "%.2f".format(data[position].usage) + "kWh"
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

