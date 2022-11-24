package com.example.electricityapplication

import java.util.Random

class DeviceData {
    var devicegroup1: ArrayList<Device> = arrayListOf()
    var devicegroup2: ArrayList<Device> = arrayListOf()
    var devicegroup3: ArrayList<Device> = arrayListOf()
    var devicegroup4: ArrayList<Device> = arrayListOf()
    var devicegroup5: ArrayList<Device> = arrayListOf()
    var devicegroup6: ArrayList<Device> = arrayListOf()
    var devices: ArrayList<DeviceGroup> = arrayListOf()
    var cost : Int
    companion object{
        private var Instance : DeviceData? = null

        fun getDeviceData() : DeviceData?{
            if(Instance == null){
                Instance = DeviceData()
            }
            return Instance
        }
    }

    init {
        var randomUsage = kotlin.random.Random.nextDouble()*5
         cost = 2

        devicegroup1.add(Device("Oven", randomUsage,randomUsage*cost))
        randomUsage = kotlin.random.Random.nextDouble()*5
        devicegroup1.add(Device("Refrigerator", randomUsage, randomUsage*cost))
        randomUsage = kotlin.random.Random.nextDouble()*5
        devicegroup1.add(Device("Coffee Machine", randomUsage, randomUsage*cost))

        randomUsage = kotlin.random.Random.nextDouble()*5
        devicegroup2.add(Device("TV", randomUsage, randomUsage*cost))
        randomUsage = kotlin.random.Random.nextDouble()*5
        devicegroup2.add(Device("Lamp", randomUsage, randomUsage*cost))


        randomUsage = kotlin.random.Random.nextDouble()*5
        devicegroup3.add(Device("Hair Dryer", randomUsage, randomUsage*cost))
        randomUsage = kotlin.random.Random.nextDouble()*5
        devicegroup3.add(Device("Electric Toothbrush", randomUsage, randomUsage*cost))


        randomUsage = kotlin.random.Random.nextDouble()*5
        devicegroup4.add(Device("Water Heater", randomUsage, randomUsage*cost))
        randomUsage = kotlin.random.Random.nextDouble()*5
        devicegroup5.add(Device("Bed Light", randomUsage, randomUsage*cost))
        randomUsage = kotlin.random.Random.nextDouble()*5
        devicegroup5.add(Device("TV", randomUsage, randomUsage*cost))
        randomUsage = kotlin.random.Random.nextDouble()*5
        devicegroup6.add(Device("Playstation", randomUsage, randomUsage*cost))
        randomUsage = kotlin.random.Random.nextDouble()*5
        devicegroup6.add(Device("TV", randomUsage, randomUsage*cost))


        devices.add(DeviceGroup(devicegroup1, "Kitchen"))
        devices.add(DeviceGroup(devicegroup2, "Living room"))
        devices.add(DeviceGroup(devicegroup3, "Bathroom"))
        devices.add(DeviceGroup(devicegroup4, "Cellar"))
        devices.add(DeviceGroup(devicegroup5, "Bedroom"))
        devices.add(DeviceGroup(devicegroup6, "Kids Room"))

    }
}