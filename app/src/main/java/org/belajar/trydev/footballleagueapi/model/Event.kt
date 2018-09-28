package org.belajar.trydev.footballleagueapi.model

import com.google.gson.annotations.SerializedName
import java.text.DateFormat
import java.util.*

data class Event(
        @SerializedName("idEvent")
        var leagueId: String? = null,

        @SerializedName("strEvent")
        var eventName: String? = null,

        @SerializedName("strHomeTeam")
        var homeTeam: String? =null,

        @SerializedName("strAwayTeam")
        var awayTeam:String? = null,

        @SerializedName("intHomeScore")
        var homeScore:String? = null,

        @SerializedName("intAwayScore")
        var awayScore:String? = null,

        @SerializedName("idHomeTeam")
        var homeTeamId:String? = null,

        @SerializedName("idAwayTeam")
        var awayTeamId:String? = null,

        @SerializedName("dateEvent")
        var dateEvent:String? = null,

        @SerializedName("intHomeShots")
        var homeShots:String? = null,

        @SerializedName("intAwayShots")
        var awayShots:String? = null

)