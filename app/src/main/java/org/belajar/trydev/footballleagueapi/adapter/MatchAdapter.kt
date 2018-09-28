package org.belajar.trydev.footballleagueapi.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import org.belajar.trydev.footballleagueapi.R
import org.belajar.trydev.footballleagueapi.R.color.colorAccent
import org.belajar.trydev.footballleagueapi.R.id.*
import org.belajar.trydev.footballleagueapi.model.Event
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MainAdapter(private val events:List<Event>, private val listener: (Event)->Unit):RecyclerView.Adapter<LeagueViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        return LeagueViewHolder(LeagueUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = events.size


    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(events[position], listener)
    }

}

class LeagueViewHolder(view:View):RecyclerView.ViewHolder(view){
    private val homeTeam:TextView = view.find(home_team)
    private val awayTeam:TextView = view.find(away_team)
    private val homeScore:TextView = view.find(home_score)
    private val awayScore:TextView = view.find(away_score)
    private val dateEvent:TextView = view.find(R.id.date_event)

    fun bindItem(events: Event, listener: (Event)->Unit){
        homeTeam.text = events.homeTeam
        awayTeam.text = events.awayTeam

        awayScore.text = events.awayScore?.toString()
        homeScore.text = events.homeScore?.toString()

        dateEvent.text = events.dateEvent

        itemView.setOnClickListener {
            listener(events)
        }
    }
}

class LeagueUI:AnkoComponent<ViewGroup>{
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
                    textColor = ContextCompat.getColor(context, colorAccent)
                }.lparams(width = wrapContent, height = wrapContent){
                    centerHorizontally()
                }

                textView {
                    id = R.id.home_team
                    textSize = 16f
                }.lparams(width = wrapContent, height = wrapContent){
                    alignParentStart()
                    below(date_event)
                }

                textView {
                    id = R.id.home_score
                    textSize = 16f
                    textColor = Color.BLACK
                }.lparams{
                    endOf(home_team)
                    below(date_event)
                    leftMargin = dip(16)
                }

                textView("VS") {
                    id = R.id.vs
                    textSize = 16f
                }.lparams(width = wrapContent, height = wrapContent) {
                    centerHorizontally()
                    below(date_event)
                }

                textView {
                    id = R.id.away_score
                    textSize = 16f
                    textColor = Color.BLACK
                }.lparams{
                    startOf(away_team)
                    below(date_event)
                    rightMargin = dip(16)
                }

                textView {
                    id = R.id.away_team
                    textSize = 16f
                }.lparams(width = wrapContent, height = wrapContent) {
                    alignParentEnd()
                    below(date_event)
                }
            }
        }
    }

}
