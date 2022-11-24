package com.example.electricityapplication.networking

data class RecordMetadata(var total: Int, var filters : String, var sort: String, var dataset : String, var records : ArrayList<Record>){

}
