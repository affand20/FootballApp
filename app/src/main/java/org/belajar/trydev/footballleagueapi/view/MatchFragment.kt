package org.belajar.trydev.footballleagueapi.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.google.gson.Gson
import org.belajar.trydev.footballleagueapi.R
import org.belajar.trydev.footballleagueapi.R.array.league_list
import org.belajar.trydev.footballleagueapi.R.id.*
import org.belajar.trydev.footballleagueapi.adapter.MainAdapter
import org.belajar.trydev.footballleagueapi.api.ApiRepository
import org.belajar.trydev.footballleagueapi.invisible
import org.belajar.trydev.footballleagueapi.model.Event
import org.belajar.trydev.footballleagueapi.model.Player
import org.belajar.trydev.footballleagueapi.model.TeamDetail
import org.belajar.trydev.footballleagueapi.model.Teams
import org.belajar.trydev.footballleagueapi.presenter.MatchPresenter
import org.belajar.trydev.footballleagueapi.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.jetbrains.anko.sdk15.coroutines.onQueryTextListener
import org.jetbrains.anko.support.v4.*
import android.support.v4.view.MenuItemCompat.getActionView
import android.app.SearchManager
import android.content.Intent
import android.util.Log
import org.jetbrains.anko.sdk25.coroutines.onQueryTextListener


class MatchFragment:Fragment(), MatchView, AnkoComponent<Context> {

    var back:Boolean = false

    private lateinit var list: RecyclerView

    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var next: Button
    private lateinit var past: Button
    private lateinit var confirm: Button
    private lateinit var spinner: Spinner
    private lateinit var search: EditText

    private var events:MutableList<Event> = mutableListOf()

    private lateinit var presenter: MatchPresenter
    private lateinit var adapter: MainAdapter
    private var eventId:String = "4328"

    private val idEvent = arrayOf("4328", "4329", "4331", "4332", "4334", "4335")

    private var isNext:Int = -1
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MainAdapter(events){
            startActivity<DetailMatchActivity>("eventId" to it.leagueId)
        }
        list.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = MatchPresenter(this, request, gson)

        presenter.getPastEvent(eventId)



        swipeRefresh.onRefresh {
            progressBar.invisible()
            if (isNext==-1){
                presenter.getPastEvent(eventId)
            }else{
                presenter.getNextEvent(eventId)
            }
        }

            showLoading()
            events.clear()
            adapter.notifyDataSetChanged()

        next.onClick {
            if (isNext!=1){
                isNext = 1
                presenter.getNextEvent(eventId)
                showLoading()
                events.clear()
                adapter.notifyDataSetChanged()
            }
        }

        past.onClick {
            if (isNext!=-1){
                isNext = -1
                presenter.getPastEvent(eventId)
                showLoading()
                events.clear()
                adapter.notifyDataSetChanged()
            }
        }

        spinner.adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(league_list))

        spinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                eventId = idEvent[position]
                if (isNext==-1){
                    isNext = -1
                    presenter.getPastEvent(eventId)
                    showLoading()
                    events.clear()
                    adapter.notifyDataSetChanged()
                }
                if (isNext==1){
                    isNext = 1
                    presenter.getNextEvent(eventId)
                    showLoading()
                    events.clear()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }

        confirm.onClick {
            if (!search.text.isEmpty()){
                val query = search.text.toString()
                presenter.getEventBySearch(query)
                showLoading()
                events.clear()
                adapter.notifyDataSetChanged()
            }
        }

        check()

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

                    search = editText{
                        id = search_bar
                        hint = "Find Match Here"
                    }.lparams(width = wrapContent)

                    confirm = button("Find"){
                    }.lparams {
                        endOf(search_bar)
                    }

                    spinner = spinner{
                        id = spinner_league
                    }.lparams{
                        below(search_bar)
                    }

                    next = button("Next Match"){
                        id = btn_next
                    }.lparams(width = wrapContent, height = wrapContent){
                        below(spinner_league)
                    }

                    past = button("Past Match"){
                        id = btn_prev
                    }.lparams(width = wrapContent, height = wrapContent){
                        endOf(next)
                        below(spinner_league)
                    }

                    list = recyclerView {
                        id = rv_match
                        lparams(width = matchParent, height = matchParent)
                        layoutManager = LinearLayoutManager(ctx)
                    }.lparams{
                        below(next)
                        topMargin = dip(16)
                    }

                    progressBar = progressBar {
                    }.lparams{
                        centerHorizontally()
                        below(next)
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
        data?.let {
            Log.i("MATCH LIST SIZE", data.size.toString())
            swipeRefresh.isRefreshing = false
            events.clear()
            events.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun showBadgeTeam(teamDetail:List<TeamDetail>?, targetId: ImageView) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTeamList(team: List<Teams>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun showPlayerList(player: List<Player>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun check(){
        back = true
        val pass:Intent = Intent().putExtra("back", back)
        activity?.setResult(Activity.RESULT_OK, pass)
    }
}
