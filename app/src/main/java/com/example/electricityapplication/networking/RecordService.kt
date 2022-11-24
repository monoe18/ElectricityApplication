package com.example.electricityapplication.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecordService {
    @GET("/dataset/Elspotprices?sort=HourDK DESC&timezone=dk")
    fun getRecords(@Query("offset") offset: Int = 0,
                   @Query("start") startDate: String,
                   @Query("end") endDate:String,
                   @Query("filter") filter :String = "{\"PriceArea\":[\"DK1\"]}") : Call<RecordMetadata>
}


//"/dataset/Elspotprices?offset=0&start=2022-11-21T00:00&end=2022-11-22T00:00&filter=%7B\"PriceArea\":[\"DK1\"]%7D&sort=HourDK%20DESC&timezone=dk"