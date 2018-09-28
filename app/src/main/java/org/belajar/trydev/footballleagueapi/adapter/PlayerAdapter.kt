package org.belajar.trydev.footballleagueapi.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.belajar.trydev.footballleagueapi.model.Player
import org.jetbrains.anko.*
import org.belajar.trydev.footballleagueapi.R.id.*

class PlayerAdapter(val context: Context, private val players:List<Player>, private val listener: (Player)->Unit): RecyclerView.Adapter<PlayersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        return PlayersViewHolder(PlayerUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        holder.bindItem(context, players[position], listener)
    }
}

class PlayersViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val playerName:TextView = view.find(player_name)
    private val playerPhoto:ImageView = view.find(player_photo)

    fun bindItem(context: Context, player:Player, listener: (Player) -> Unit){
        playerName.text = player.strPlayer
        Glide.with(context).load(player.photo).into(playerPhoto)

        itemView.setOnClickListener {
            listener(player)
        }
    }
}

class PlayerUI:AnkoComponent<ViewGroup>{
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui){
            relativeLayout {
                lparams(width = matchParent, height = wrapContent)
                padding = dip(16)

                imageView {
                    id = player_photo
                }.lparams(width = dip(50), height = dip(50))

                textView{
                    id = player_name
                }.lparams(width = matchParent, height = wrapContent){
                    endOf(player_photo)
                    leftMargin = dip(16)
                }
            }
        }
    }

}
