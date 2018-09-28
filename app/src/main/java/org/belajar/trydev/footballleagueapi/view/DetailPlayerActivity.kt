package org.belajar.trydev.footballleagueapi.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import org.belajar.trydev.footballleagueapi.R.id.*
import org.jetbrains.anko.*

class DetailPlayerActivity:AppCompatActivity(), AnkoLogger {

    private lateinit var fanArt:ImageView
    private lateinit var desc:TextView
    private lateinit var role:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playerName = intent.getStringExtra("playerName")
        val description = intent.getStringExtra("description")
        val position = intent.getStringExtra("position")
        val banner = intent.getStringExtra("banner")

        scrollView {
            relativeLayout {
                fanArt = imageView {
                    id = fanart
                }.lparams(width = matchParent, height = dip(200))

                desc = textView{
                    id = deskripsi
                }.lparams{
                    below(view)
                    topMargin = dip(16)
                }

                role = textView{
                    id = pos
                    textSize = 25F
                }.lparams{
                    below(fanart)
                }

                view{
                    id = view
                }.lparams(width = matchParent, height = dip(3)){
                    below(pos)
                }
            }
        }

        Glide.with(this).load(banner).into(fanArt)
        role.text = position
        desc.text = description
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = playerName
    }
}