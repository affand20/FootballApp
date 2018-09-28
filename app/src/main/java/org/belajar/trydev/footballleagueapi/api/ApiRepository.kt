package org.belajar.trydev.footballleagueapi.api

import android.util.Log
import java.net.URL

class ApiRepository {
    fun doRequest(url:String):String{
        return URL(url).readText()
    }
}