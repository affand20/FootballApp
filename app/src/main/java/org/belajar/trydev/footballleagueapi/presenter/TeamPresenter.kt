package org.belajar.trydev.footballleagueapi.presenter

import com.google.gson.Gson
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.belajar.trydev.footballleagueapi.CoroutineContextProvider
import org.belajar.trydev.footballleagueapi.api.ApiRepository
import org.belajar.trydev.footballleagueapi.api.TheSportDBApi
import org.belajar.trydev.footballleagueapi.model.PlayerResponse
import org.belajar.trydev.footballleagueapi.model.TeamsResponse
import org.belajar.trydev.footballleagueapi.view.MatchView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.coroutines.experimental.bg

class TeamPresenter(private val view: MatchView, private val apiRepository: ApiRepository, private val gson: Gson, private val context: CoroutineContextProvider = CoroutineContextProvider()):AnkoLogger {

    fun getTeams(league:String?){
        view.showLoading()
        async(UI){
            val team = bg{
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeam(league)),
                        TeamsResponse::class.java)
            }
            view.showTeamList(team.await().teams)
            view.hideLoading()
        }
    }

    fun getPlayer(teamId:String?){
        view.showLoading()
        async(UI){
            val player = bg{
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getListPlayer(teamId)),
                        PlayerResponse::class.java)
            }
            view.showPlayerList(player.await().players)
            view.hideLoading()
        }
    }

    fun getTeamsBySearch(teamName:String?){
        view.showLoading()
        async(UI){
            val team = bg{
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.findTeam(teamName)),
                        TeamsResponse::class.java)
            }
            view.showTeamList(team.await().teams)
            view.hideLoading()
        }
    }
}