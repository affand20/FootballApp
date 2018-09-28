package org.belajar.trydev.footballleagueapi.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.coroutines.experimental.selects.select
import org.belajar.trydev.footballleagueapi.R.color.colorAccent
import org.belajar.trydev.footballleagueapi.R.id.*
import org.belajar.trydev.footballleagueapi.adapter.FavoriteMatchAdapter
import org.belajar.trydev.footballleagueapi.adapter.FavoriteTeamsAdapter
import org.belajar.trydev.footballleagueapi.db.Favorite
import org.belajar.trydev.footballleagueapi.db.FavoriteTeam
import org.belajar.trydev.footballleagueapi.db.database
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import org.jetbrains.anko.support.v4.toast

class FavoriteMatchFragment:Fragment(), AnkoComponent<Context> {

    private var favorites: MutableList<Favorite> = mutableListOf()
    private var favoriteTeams: MutableList<FavoriteTeam> = mutableListOf()

    private lateinit var adapter: FavoriteMatchAdapter
    private lateinit var adapterFavTeam: FavoriteTeamsAdapter

    private lateinit var listEvent: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var match:Button
    private lateinit var teams:Button

    private var teamOrMatch:String = "match"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = FavoriteMatchAdapter(favorites){
            ctx.startActivity<DetailMatchActivity>("eventId" to "${it.eventId}")
        }

        adapterFavTeam = FavoriteTeamsAdapter(ctx, favoriteTeams){
            ctx.startActivity<DetailTeamActivity>("teamId" to it.teamId,
                    "teamName" to it.teamName,
                    "description" to it.description,
                    "teamBadge" to it.teamBadge)
        }

        favorites.clear()
        favoriteTeams.clear()

        if (teamOrMatch.equals("match")){
            listEvent.adapter = adapter
            showFavorite()
        }
        if (teamOrMatch.equals("team")){
            listEvent.adapter = adapterFavTeam
            showFavorite()
        }


        swipeRefresh.onRefresh {
            favorites.clear()
            favoriteTeams.clear()
            if (teamOrMatch.equals("match")){
                listEvent.adapter = adapter
                showFavorite()
            }
            if (teamOrMatch.equals("team")){
                listEvent.adapter = adapterFavTeam
                showFavoriteTeam()
            }
        }

        match.onClick {
            teamOrMatch = "match"
            favorites.clear()
            listEvent.adapter = adapter
            showFavorite()
        }

        teams.onClick {
            teamOrMatch = "team"
            favoriteTeams.clear()
            listEvent.adapter = adapterFavTeam
            showFavoriteTeam()
        }
    }

    private fun showFavorite(){
        context?.database?.use {
            swipeRefresh.isRefreshing = false
            val result = select(Favorite.TABLE_FAVORITE)
            val favorite = result.parseList(classParser<Favorite>())
            favorites.addAll(favorite)
            adapter.notifyDataSetChanged()
            if (favorites.size<0){
                toast("Favorite Match is Empty")
            }
        }
    }

    private fun showFavoriteTeam(){
        context?.database?.use {
            swipeRefresh.isRefreshing = false
            val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
            val favoriteTeam = result.parseList(classParser<FavoriteTeam>())
            favoriteTeams.addAll(favoriteTeam)
            adapterFavTeam.notifyDataSetChanged()
            if (favoriteTeams.size<0){
                toast("Favorite Teams is Empty")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui){
        linearLayout {
            lparams (width = matchParent, height = wrapContent)
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)

            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light)

                relativeLayout{
                    lparams(width = matchParent, height = matchParent)

                    match = button("Match Favorite"){
                        allCaps = false
                        id = match_btn
                    }.lparams(width = wrapContent, height = wrapContent)

                    teams = button("Teams Favorite"){
                        allCaps = false
                        id = teams_btn
                    }.lparams(width = wrapContent, height = wrapContent){
                        endOf(match_btn)
                    }

                    listEvent = recyclerView {
                        lparams (width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }.lparams{
                        below(teams_btn)
                    }
                }

            }
        }
    }

}