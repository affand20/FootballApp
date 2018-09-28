package org.belajar.trydev.footballleagueapi.api

import org.belajar.trydev.footballleagueapi.BuildConfig
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

object TheSportDBApi:AnkoLogger {
    fun getPastEvent(eventId:String?):String{
        return BuildConfig.BASE_URL+"api/v1/json/${BuildConfig.TSDB_API_KEY}"+"/eventspastleague.php?id="+eventId
    }

    fun getNextEvent(eventId:String?):String{
        return BuildConfig.BASE_URL+"api/v1/json/${BuildConfig.TSDB_API_KEY}"+"/eventsnextleague.php?id="+eventId
    }

    fun getBadgeTeam(id:String?):String{
        return BuildConfig.BASE_URL+"api/v1/json/${BuildConfig.TSDB_API_KEY}"+"/lookupteam.php?id="+id
    }

    fun getDetailMatch(eventId: String?):String{
        return BuildConfig.BASE_URL+"api/v1/json/${BuildConfig.TSDB_API_KEY}"+"/lookupevent.php?id="+eventId
    }

    fun getTeam(league:String?):String{
        return BuildConfig.BASE_URL+"api/v1/json/${BuildConfig.TSDB_API_KEY}"+"/search_all_teams.php?l="+league
    }

    fun getListPlayer(teamId:String?):String{
        return BuildConfig.BASE_URL+"api/v1/json/${BuildConfig.TSDB_API_KEY}"+
                "/lookup_all_players.php?id="+teamId
    }

    fun findTeam(teamName:String?):String{
        return BuildConfig.BASE_URL+"api/v1/json/${BuildConfig.TSDB_API_KEY}"+"/searchteams.php?t="+teamName
    }

    fun findEvent(eventName:String?):String{
        return BuildConfig.BASE_URL+"api/v1/json/${BuildConfig.TSDB_API_KEY}"+"/searchevents.php?e="+eventName
    }

}