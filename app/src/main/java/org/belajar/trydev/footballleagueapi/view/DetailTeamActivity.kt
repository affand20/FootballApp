package org.belajar.trydev.footballleagueapi.view

import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.belajar.trydev.footballleagueapi.R
import org.belajar.trydev.footballleagueapi.R.id.*
import org.belajar.trydev.footballleagueapi.R.menu.detail_menu
import org.belajar.trydev.footballleagueapi.adapter.PlayerAdapter
import org.belajar.trydev.footballleagueapi.api.ApiRepository
import org.belajar.trydev.footballleagueapi.db.FavoriteTeam
import org.belajar.trydev.footballleagueapi.db.database
import org.belajar.trydev.footballleagueapi.invisible
import org.belajar.trydev.footballleagueapi.model.Event
import org.belajar.trydev.footballleagueapi.model.Player
import org.belajar.trydev.footballleagueapi.model.TeamDetail
import org.belajar.trydev.footballleagueapi.model.Teams
import org.belajar.trydev.footballleagueapi.presenter.TeamPresenter
import org.belajar.trydev.footballleagueapi.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView

class DetailTeamActivity : AppCompatActivity(), MatchView, AnkoLogger {
    private lateinit var teamId:String
    private lateinit var teamBadge:String
    private lateinit var teamName:String
    private lateinit var description:String

    private lateinit var progressBar: ProgressBar
    private lateinit var teambadge:ImageView
    private lateinit var desc:TextView
    private lateinit var teamname:TextView
    private lateinit var list:RecyclerView

    private var playersList:MutableList<Player> = mutableListOf()
    private lateinit var presenter: TeamPresenter
    private lateinit var adapter: PlayerAdapter

    private var menuItem: Menu? = null
    private var isFavorite:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scrollView {
            relativeLayout {
                lparams(width = matchParent, height = matchParent)
                padding = dip(16)

                progressBar = progressBar {
                }.lparams{
                    centerHorizontally()
                }

                teambadge = imageView{
                    id = team_badge
                }.lparams(width = dip(70), height = dip(70)){
                    centerHorizontally()
                }

                teamname = textView{
                    id = team_name
                    textSize = 20F
                }.lparams{
                    below(team_badge)
                    centerHorizontally()
                    bottomMargin = dip(16)
                }

                desc = textView{
                    id = overview
                }.lparams{
                    below(team_name)
                    bottomMargin = dip(16)
                }

                textView("Players"){
                    id = player_list_title
                }.lparams{
                    below(overview)
                }

                list = recyclerView {
                    id = rv_players
                    layoutManager = LinearLayoutManager(ctx)
                }.lparams(width = matchParent, height = wrapContent){
                    below(player_list_title)
                }
            }
        }

        teamId = intent.getStringExtra("teamId")
        info("TEAM ID : "+teamId)
        teamName = intent.getStringExtra("teamName")
        teamBadge = intent.getStringExtra("teamBadge")
        description = intent.getStringExtra("description")

        teamname.text = teamName
        desc.text = description
        Glide.with(ctx).load(teamBadge).into(teambadge)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = teamName

        adapter = PlayerAdapter(ctx, playersList){
            startActivity<DetailPlayerActivity>(
                    "playerName" to it.strPlayer,
                    "description" to it.description,
                    "position" to it.position,
                    "banner" to it.banner
            )
        }

        list.adapter = adapter

        favoriteState()

        val request = ApiRepository()
        val gson = Gson()
        presenter = TeamPresenter(this, request, gson)
        presenter.getPlayer(teamId)

        playersList.clear()
        adapter.notifyDataSetChanged()


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
                if (isFavorite){
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

    private fun favoriteState(){
        database.use {
            val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
                    .whereArgs("(TEAM_ID = {id})",
                            "id" to teamId)
            val favorite = result?.parseList(classParser<FavoriteTeam>())
            if (!favorite.isEmpty()){
                isFavorite = true
            }
        }
    }

    private fun addToFavorite(){
        try{
            database.use {
                insert(FavoriteTeam.TABLE_FAVORITE_TEAM,
                        FavoriteTeam.TEAM_ID to teamId,
                        FavoriteTeam.TEAM_NAME to teamName,
                        FavoriteTeam.TEAM_BADGE to teamBadge,
                        FavoriteTeam.DESCRIPTION to description
                )
            }
            toast("Ditambahkan ke Favorit").show()
        }catch (e:SQLiteConstraintException){
            toast(e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try{
            database.use{
                delete(FavoriteTeam.TABLE_FAVORITE_TEAM, "(TEAM_ID = {id})",
                        "id" to teamId)
            }
            toast("Dihapus dari Favorit").show()
        }catch (e:SQLiteConstraintException){
            toast(e.localizedMessage).show()
        }
    }

    private fun setFavorite(){
        if (isFavorite){
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp)
        }
        else{
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_border_black_24dp)
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showLeagueList(data: List<Event>?) {}

    override fun showBadgeTeam(teamDetail: List<TeamDetail>?, targetId: ImageView) {}

    override fun showTeamList(team: List<Teams>?) {}

    override fun showPlayerList(player: List<Player>?) {
        player?.let{
            info("MASUK2")
            playersList.clear()
            playersList.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }
}
