package org.belajar.trydev.footballleagueapi.presenter

import android.util.Log
import android.widget.ImageView
import com.google.gson.Gson
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.belajar.trydev.footballleagueapi.CoroutineContextProvider
import org.belajar.trydev.footballleagueapi.api.ApiRepository
import org.belajar.trydev.footballleagueapi.api.TheSportDBApi
import org.belajar.trydev.footballleagueapi.model.*
import org.belajar.trydev.footballleagueapi.view.MatchView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread

class MatchPresenter(private val view:MatchView, private val apiRepository: ApiRepository, private val gson: Gson, private val context:CoroutineContextProvider = CoroutineContextProvider()):AnkoLogger {

    fun getPastEvent(eventId:String?){

        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getPastEvent(eventId)),
                        EventResponse::class.java
                )
            }
            view.showLeagueList(data.await().events)
            view.hideLoading()
        }
    }

    fun getNextEvent(eventId:String?){

        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getNextEvent(eventId)),
                        EventResponse::class.java
                )
            }

            view.showLeagueList(data.await().events)
            view.hideLoading()
        }
    }

    fun getBadgeTeam(teamId:String?, targetId:ImageView){
        async(UI) {
            val badge = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getBadgeTeam(teamId)),
                        TeamDetailResponse::class.java)
            }
            info(badge.await().teamDetails.size)
            view.showBadgeTeam(badge.await().teamDetails, targetId)
        }

    }

    fun getDetailMatch(eventId:String?){
        view.showLoading()
        async(UI){
            val detail = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getDetailMatch(eventId)),
                        EventResponse::class.java)
            }
            view.showLeagueList(detail.await().events)
            view.hideLoading()
        }
    }

    fun getEventBySearch(teamName:String?){
        view.showLoading()
        async(UI){
            val team = bg{
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.findEvent(teamName)),
                        SearchEventResponse::class.java)
            }
            view.showLeagueList(team.await().events)
            view.hideLoading()
        }
    }

}