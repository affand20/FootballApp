package org.belajar.trydev.footballleagueapi.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.belajar.trydev.footballleagueapi.model.Teams
import org.jetbrains.anko.*
import org.belajar.trydev.footballleagueapi.R.id.*

class TeamsAdapter(val context: Context, private val teams:List<Teams>, private val listener: (Teams)->Unit): RecyclerView.Adapter<TeamsViewHolder>(), AnkoLogger {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
        return TeamsViewHolder(TeamUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = teams.size

    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        holder.bindItem(context, teams[position], listener)
    }
}

class TeamsViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val teamName:TextView = view.find(team_name)
    private val teamBadge:ImageView = view.find(team_badge)

    fun bindItem(context: Context,team:Teams, listener: (Teams) -> Unit){
        teamName.text = team.strTeam
        Glide.with(context).load(team.teamBadge).into(teamBadge)

        itemView.setOnClickListener{
            listener(team)
        }
    }
}

class TeamUI:AnkoComponent<ViewGroup>{
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui){
            relativeLayout{
                lparams(width = matchParent, height = wrapContent)
                padding = dip(16)

                imageView {
                    id = team_badge
                }.lparams(width = dip(50), height = dip(50))

                textView {
                    id = team_name
                }.lparams(width = matchParent, height = wrapContent){
                    endOf(team_badge)
                    centerVertically()
                    leftMargin = dip(16)
                }
            }
        }
    }

}