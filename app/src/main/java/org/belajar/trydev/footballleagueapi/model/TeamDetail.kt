package org.belajar.trydev.footballleagueapi.model

import com.google.gson.annotations.SerializedName

data class TeamDetail (
        @SerializedName("strTeamBadge")
        var teamBadge:String? = null
)