package com.example.electricityapplication

import android.content.Context
import android.content.Intent
import android.service.autofill.OnClickAction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MainAdapter(private val data : ArrayList<DeviceGroup>, var context : Context) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    inner class ViewHolder(item : View): RecyclerView.ViewHolder(item){
        val groupName : TextView = item.findViewById(R.id.groupName)
        val deviceno : TextView = item.findViewById(R.id.deviceAmount)
        val consumption : TextView = item.findViewById(R.id.consumption)
        val totalcost : TextView = item.findViewById(R.id.totalcost)
        init {
            item.setOnClickListener(object: View.OnClickListener {
                override fun onClick(view: View){
                    val intent = Intent(context, DeviceList::class.java).apply{
                        putExtra("groupname", groupName.text)
                    }
                    startActivity(context,intent,null)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mainviewholder, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.groupName.text = data[position].groupname
        holder.deviceno.text = "No. Devices: " + data[position].devices.size.toString()
        var totalconsumption : Double = 0.0
        var totalcost :Double = 0.0
        for(device in data[position].devices){
            totalconsumption+= device.usage
            if(device.cost!= null){
                totalcost += device.cost!!
            }
        }
        holder.consumption.text = "%.2f".format(totalconsumption) +" kWh"
        holder.totalcost.text = "%.2f".format(totalcost) +" DKK"
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

