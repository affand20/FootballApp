package org.belajar.trydev.footballleagueapi.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import org.belajar.trydev.footballleagueapi.R
import org.belajar.trydev.footballleagueapi.R.id.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class HomeActivity : AppCompatActivity(), AnkoLogger {

    var kembali:Boolean? = false

    override fun onBackPressed() {
        val back:Boolean = MatchFragment().back
        info("back value: "+kembali)
//        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        info("from activity "+data?.getBooleanExtra("back", false))
//        kembali = data?.getBooleanExtra("back", false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                teams -> {
                    loadNextMatchFragment(savedInstanceState)
                }
                favorites -> {
                    loadFavoritesFragment(savedInstanceState)
                }
                match -> {
                    loadPrevMatchFragment(savedInstanceState)
                }
            }
            true
        }

        bottom_navigation.selectedItemId = match
    }

    private fun loadPrevMatchFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null){

            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, MatchFragment(), MatchFragment::class.simpleName)
                    .commit()
        }
    }

    private fun loadNextMatchFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null){
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, TeamsFragment(), TeamsFragment::class.simpleName)
                    .commit()
        }
    }

    private fun loadFavoritesFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null){
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, FavoriteMatchFragment(), FavoriteMatchFragment::class.simpleName)
                    .commit()
        }
    }
}
