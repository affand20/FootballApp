package org.belajar.trydev.footballleagueapi.view

import android.widget.ImageView
import org.belajar.trydev.footballleagueapi.model.Event
import org.belajar.trydev.footballleagueapi.model.Player
import org.belajar.trydev.footballleagueapi.model.TeamDetail
import org.belajar.trydev.footballleagueapi.model.Teams

interface MatchView {
    fun showLoading()
    fun hideLoading()
    fun showLeagueList(data:List<Event>?)
    fun showBadgeTeam(teamDetail:List<TeamDetail>?, targetId:ImageView)
    fun showTeamList(team:List<Teams>?)
    fun showPlayerList(player:List<Player>?)
}