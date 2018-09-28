package org.belajar.trydev.footballleagueapi.view

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.belajar.trydev.footballleagueapi.R.color.colorAccent
import org.belajar.trydev.footballleagueapi.R.drawable.ic_star_black_24dp
import org.belajar.trydev.footballleagueapi.R.drawable.ic_star_border_black_24dp
import org.belajar.trydev.footballleagueapi.R.id.*
import org.belajar.trydev.footballleagueapi.R.menu.detail_menu
import org.belajar.trydev.footballleagueapi.api.ApiRepository
import org.belajar.trydev.footballleagueapi.db.Favorite
import org.belajar.trydev.footballleagueapi.model.Event
import org.belajar.trydev.footballleagueapi.model.TeamDetail
import org.belajar.trydev.footballleagueapi.db.database
import org.belajar.trydev.footballleagueapi.invisible
import org.belajar.trydev.footballleagueapi.model.Player
import org.belajar.trydev.footballleagueapi.model.Teams
import org.belajar.trydev.footballleagueapi.presenter.MatchPresenter
import org.belajar.trydev.footballleagueapi.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class DetailMatchActivity : AppCompatActivity(), MatchView {

    private lateinit var progressBar: ProgressBar
    private lateinit var vsTextView: TextView
    private lateinit var shotsTextView: TextView

    private lateinit var presenter:MatchPresenter
    private var menuItem: Menu? = null
    private var isFavorite:Boolean = false
    private lateinit var dateEvent:TextView
    private lateinit var homeTeam:TextView
    private lateinit var awayTeam:TextView
    private lateinit var homeScore:TextView
    private lateinit var awayScore:TextView
    private lateinit var homeBadge:ImageView
    private lateinit var awayBadge:ImageView
    private lateinit var homeShots:TextView
    private lateinit var awayShots:TextView
    private lateinit var homeTeamId:String
    private lateinit var awayTeamId:String

    private lateinit var detail:Event

    private lateinit var eventId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eventId = intent.getStringExtra("eventId")

        supportActionBar?.title = "Match Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        relativeLayout{
            lparams(width = matchParent, height = matchParent)
            padding = dip(16)

            progressBar = progressBar {
            }.lparams{
                centerHorizontally()
            }

            relativeLayout{
                lparams(width = matchParent, height = wrapContent)
                id = score_layout

                textView {
                    id = date_event
                    textColor = ContextCompat.getColor(context, colorAccent)
                    textSize = 16f
                }.lparams {
                    centerHorizontally()
                }

                imageView {
                    id = home_badge
                }.lparams(width = dip(100), height = dip(100)){
                    alignParentStart()
                    below(date_event)
                }
                textView {
                    id = home_team
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                }.lparams(width = dip(100), height = wrapContent) {
                    below(home_badge)
                    topMargin = dip(5)
                    alignParentStart()
                }


                textView {
                    id = home_score
                    textSize = 25f
                    textColor = Color.BLACK
                }.lparams {
                    startOf(vs)
                    below(date_event)
                    rightMargin = dip(16)
                    topMargin = dip(25)
                }
                vsTextView = textView("VS"){
                    id = vs
                }.lparams {
                    below(date_event)
                    centerHorizontally()
                    topMargin = dip(25)
                }
                textView {
                    id = away_score
                    textSize = 25f
                    textColor = Color.BLACK
                }.lparams {
                    endOf(vs)
                    below(date_event)
                    leftMargin = dip(16)
                    topMargin = dip(25)
                }


                imageView {
                    id = away_badge
                }.lparams(width = dip(100), height = dip(100)){
                    alignParentEnd()
                    below(date_event)
                }
                textView {
                    id = away_team
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                }.lparams(width = dip(100), height = wrapContent){
                    alignParentEnd()
                    topMargin = dip(5)
                    below(away_badge)
                }
            }

            shotsTextView = textView("Shots"){
                textColor = ContextCompat.getColor(context, colorAccent)
                id = shots
                textSize = 16f
            }.lparams{
                topMargin = dip(20)
                centerHorizontally()
                below(score_layout)
            }
            textView{
                textColor = Color.BLACK
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                id = home_shots
            }.lparams(width = dip(100), height = wrapContent){
                below(shots)
                alignParentStart()
                topMargin = dip(5)
            }
            textView{
                textColor = Color.BLACK
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                id = away_shots
            }.lparams(width = dip(100), height = wrapContent){
                below(shots)
                alignParentEnd()
                topMargin = dip(5)
            }

        }

        supportActionBar?.title = "Match Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dateEvent = find(date_event)
        homeTeam = find(home_team)
        awayTeam = find(away_team)
        homeScore = find(home_score)
        awayScore = find(away_score)
        homeBadge = find(home_badge)
        awayBadge = find(away_badge)
        homeShots = find(home_shots)
        awayShots = find(away_shots)

        favoriteState()

        val request = ApiRepository()
        val gson = Gson()
        presenter = MatchPresenter(this, request, gson)
        presenter.getDetailMatch(eventId)

    }

    private fun favoriteState(){
        database.use {
            val result = select(Favorite.TABLE_FAVORITE)
                    .whereArgs("(EVENT_ID = {id})",
                            "id" to eventId)
            val favorite = result.parseList(classParser<Favorite>())
            if (!favorite.isEmpty()){
                isFavorite = true
            }
        }
    }

    override fun showLoading() {
        vsTextView.invisible()
        shotsTextView.invisible()
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
        vsTextView.visible()
        shotsTextView.visible()
    }

    override fun showTeamList(team: List<Teams>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLeagueList(data: List<Event>?) {
        data?.let {
            detail = data.get(0)
            homeTeamId = it.get(0).homeTeamId.toString()
            awayTeamId = it.get(0).awayTeamId.toString()

            dateEvent.text = it.get(0).dateEvent
            homeTeam.text = it.get(0).homeTeam
            awayTeam.text = it.get(0).awayTeam
            homeScore.text = it.get(0).homeScore
            awayScore.text = it.get(0).awayScore
            homeShots.text = it.get(0).homeShots
            awayShots.text = it.get(0).awayShots
        }

        val badgePresenter = MatchPresenter(this, ApiRepository(),Gson())
        badgePresenter.getBadgeTeam(homeTeamId, homeBadge)
        badgePresenter.getBadgeTeam(awayTeamId, awayBadge)
    }

    override fun showPlayerList(player: List<Player>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showBadgeTeam(teamDetail: List<TeamDetail>?, targetId:ImageView) {
        teamDetail?.let {
            Glide.with(this).load(teamDetail.get(0).teamBadge).into(targetId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            add_to_favorite -> {
                if (isFavorite) {
                    removeFromFavorite()
                } else{
                    addToFavorite()
                }

                isFavorite = !isFavorite
                setFavorite()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToFavorite(){
        try{
            database.use{
                insert(Favorite.TABLE_FAVORITE,
                        Favorite.EVENT_ID to detail.leagueId,
                        Favorite.EVENT_NAME to detail.eventName,
                        Favorite.DATE_EVENT to detail.dateEvent,
                        Favorite.HOME_TEAM_ID to detail.homeTeamId,
                        Favorite.AWAY_TEAM_ID to detail.awayTeamId,
                        Favorite.HOME_TEAM_NAME to detail.homeTeam,
                        Favorite.AWAY_TEAM_NAME to detail.awayTeam,
                        Favorite.HOME_SCORE to detail.homeScore,
                        Favorite.AWAY_SCORE to detail.awayScore,
                        Favorite.HOME_SHOTS to detail.homeShots,
                        Favorite.AWAY_SHOTS to detail.awayShots
                        )
            }
            toast("Ditambahkan ke Favorit").show()
        } catch (e:SQLiteConstraintException){
            toast(e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try{
            database.use{
                delete(Favorite.TABLE_FAVORITE, "(EVENT_ID = {id})",
                        "id" to eventId)
            }
            toast("Dihapus dari Favorit").show()
        } catch(e:SQLiteConstraintException){
            toast(e.localizedMessage).show()
        }
    }

    private fun setFavorite(){
        if (isFavorite){
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_star_black_24dp)
        }
        else{
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_star_border_black_24dp)
        }
    }
}
