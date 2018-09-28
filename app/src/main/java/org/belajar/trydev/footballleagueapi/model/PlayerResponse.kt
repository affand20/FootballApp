package org.belajar.trydev.footballleagueapi.model

import com.google.gson.annotations.SerializedName

data class PlayerResponse(
        @SerializedName("player")
        val players:List<Player>?=null
)
