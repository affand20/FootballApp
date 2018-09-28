package org.belajar.trydev.footballleagueapi.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.belajar.trydev.footballleagueapi.R
import org.belajar.trydev.footballleagueapi.db.Favorite
import org.belajar.trydev.footballleagueapi.model.Event
import org.jetbrains.anko.*

class FavoriteMatchAdapter(private val favorite:List<Favorite>, private val listener: (Favorite)->Unit)
    :RecyclerView.Adapter<FavoriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(FavoriteUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = favorite.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bindItem(favorite[position], listener)
    }
}

class FavoriteUI:AnkoComponent<ViewGroup>{
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui){
            relativeLayout{
                lparams(width = matchParent, height = wrapContent){
                    topMargin = dip(5)
                    bottomMargin = dip(5)
                }
                padding = dip(16)

                textView {
                    id = R.id.date_event
                    textSize = 16f
                    textColor = ContextCompat.getColor(context, R.color.colorAccent)
                }.lparams(width = wrapContent, height = wrapContent){
                    centerHorizontally()
                }

                textView {
                    id = R.id.home_team
                    textSize = 16f
                }.lparams(width = wrapContent, height = wrapContent){
                    alignParentStart()
                    below(R.id.date_event)
                }

                textView {
                    id = R.id.home_score
                    textSize = 16f
                    textColor = Color.BLACK
                }.lparams{
                    startOf(R.id.vs)
                    below(R.id.date_event)
                    rightMargin = dip(16)
                }

                textView("VS") {
                    id = R.id.vs
                    textSize = 16f
                }.lparams(width = wrapContent, height = wrapContent) {
                    centerHorizontally()
                    below(R.id.date_event)
                }

                textView {
                    id = R.id.away_score
                    textSize = 16f
                    textColor = Color.BLACK
                }.lparams{
                    endOf(R.id.vs)
                    below(R.id.date_event)
                    leftMargin = dip(16)
                }

                textView {
                    id = R.id.away_team
                    textSize = 16f
                }.lparams(width = wrapContent, height = wrapContent) {
                    alignParentEnd()
                    below(R.id.date_event)
                }
            }
        }
    }
}

class FavoriteViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val homeTeam: TextView = view.find(R.id.home_team)
    private val awayTeam: TextView = view.find(R.id.away_team)
    private val homeScore: TextView = view.find(R.id.home_score)
    private val awayScore: TextView = view.find(R.id.away_score)
    private val dateEvent: TextView = view.find(R.id.date_event)

    fun bindItem(favorite: Favorite, listener: (Favorite)->Unit){
        homeTeam.text = favorite.homeTeamName
        awayTeam.text = favorite.awayTeamName

        awayScore.text = favorite.awayScore
        homeScore.text = favorite.homeScore

        dateEvent.text = favorite.dateEvent

        itemView.setOnClickListener {
            listener(favorite)
        }
    }
}
