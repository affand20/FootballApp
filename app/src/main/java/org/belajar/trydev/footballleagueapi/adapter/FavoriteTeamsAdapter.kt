package org.belajar.trydev.footballleagueapi.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.belajar.trydev.footballleagueapi.R
import org.belajar.trydev.footballleagueapi.db.FavoriteTeam
import org.belajar.trydev.footballleagueapi.model.Teams
import org.jetbrains.anko.*

class FavoriteTeamsAdapter(private val context: Context, private val favorite:List<FavoriteTeam>, private val listener: (FavoriteTeam)->Unit):RecyclerView.Adapter<FavoriteTeamViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTeamViewHolder {
        return FavoriteTeamViewHolder(FavoriteTeamUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = favorite.size

    override fun onBindViewHolder(holder: FavoriteTeamViewHolder, position: Int) {
        holder.bindItem(context, favorite[position], listener)
    }
}

class FavoriteTeamUI():AnkoComponent<ViewGroup>{
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui){
            relativeLayout{
                lparams(width = matchParent, height = wrapContent)
                padding = dip(16)

                imageView {
                    id = R.id.team_badge
                }.lparams(width = dip(50), height = dip(50))

                textView {
                    id = R.id.team_name
                }.lparams(width = matchParent, height = wrapContent){
                    endOf(R.id.team_badge)
                    centerVertically()
                    leftMargin = dip(16)
                }
            }
        }
    }

}

class FavoriteTeamViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val teamName: TextView = view.find(R.id.team_name)
    private val teamBadge: ImageView = view.find(R.id.team_badge)

    fun bindItem(context: Context, favoriteTeam: FavoriteTeam, listener: (FavoriteTeam) -> Unit){
        teamName.text = favoriteTeam.teamName
        Glide.with(context).load(favoriteTeam.teamBadge).into(teamBadge)

        itemView.setOnClickListener{
            listener(favoriteTeam)
        }
    }
}
