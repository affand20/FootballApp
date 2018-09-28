package org.belajar.trydev.footballleagueapi.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import org.belajar.trydev.footballleagueapi.R
import org.belajar.trydev.footballleagueapi.R.id.*
import org.belajar.trydev.footballleagueapi.R.array.league_list
import org.belajar.trydev.footballleagueapi.adapter.TeamsAdapter
import org.belajar.trydev.footballleagueapi.api.ApiRepository
import org.belajar.trydev.footballleagueapi.invisible
import org.belajar.trydev.footballleagueapi.model.Event
import org.belajar.trydev.footballleagueapi.model.Player
import org.belajar.trydev.footballleagueapi.model.TeamDetail
import org.belajar.trydev.footballleagueapi.model.Teams
import org.belajar.trydev.footballleagueapi.presenter.TeamPresenter
import org.belajar.trydev.footballleagueapi.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class TeamsFragment:Fragment(), MatchView, AnkoComponent<Context>, AnkoLogger {

    private lateinit var list: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var spinner: Spinner
    private lateinit var search:EditText
    private lateinit var confirm:Button

    private var teams:MutableList<Teams> = mutableListOf()
    private lateinit var presenter: TeamPresenter
    private lateinit var adapter: TeamsAdapter

    private lateinit var league:String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = TeamsAdapter(ctx, teams){
            startActivity<DetailTeamActivity>("teamId" to it.idTeam,
                    "teamName" to it.strTeam,
                    "description" to it.description,
                    "teamBadge" to it.teamBadge)
        }
        list.adapter = adapter

        spinner.adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(league_list))

        val request = ApiRepository()
        val gson = Gson()
        presenter = TeamPresenter(this, request, gson)

        spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                league = spinner.selectedItem.toString()
                presenter.getTeams(league)
                teams.clear()
                adapter.notifyDataSetChanged()
            }

        }

        swipeRefresh.onRefresh {
            progressBar.invisible()
            presenter.getTeams(league)
        }

        confirm.onClick {
            if (!search.text.isEmpty()){
                val query = search.text.toString()
                presenter.getTeamsBySearch(query)
                teams.clear()
                adapter.notifyDataSetChanged()
            }
        }

        showLoading()
        teams.clear()
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }



    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        linearLayout {
            lparams(width = matchParent, height = matchParent)
            orientation = LinearLayout.VERTICAL
            topPadding = dip(16)
            leftPadding = dip(16)
            rightPadding = dip(16)

            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(R.color.colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_red_light,
                        android.R.color.holo_orange_light)

                relativeLayout {
                    lparams(width = matchParent, height = matchParent)
                    search = editText {
                        id = search_bar
                        hint = "Find Team Here"
                    }.lparams(width = wrapContent)

                    confirm = button("Find"){
                    }.lparams {
                        endOf(search_bar)
                    }

                    spinner = spinner{
                        id = spinner_league
                    }.lparams {
                        below(search_bar)
                        topMargin = dip(16)
                    }

                    list = recyclerView {
                        id = rv_teams
                        lparams(width = matchParent, height = matchParent)
                        layoutManager = LinearLayoutManager(ctx)
                    }.lparams{
                        below(spinner_league)
                    }

                    progressBar = progressBar {
                    }.lparams{
                        centerHorizontally()
                        below(spinner_league)
                    }
                }
            }
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showLeagueList(data: List<Event>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showBadgeTeam(teamDetail:List<TeamDetail>?, targetId: ImageView) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTeamList(team: List<Teams>?) {
        team?.let{
            swipeRefresh.isRefreshing = false
            teams.clear()
            teams.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun showPlayerList(player: List<Player>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}