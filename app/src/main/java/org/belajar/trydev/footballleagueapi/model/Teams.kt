package org.belajar.trydev.footballleagueapi.model

import com.google.gson.annotations.SerializedName

data class Teams (
    @SerializedName("idTeam")
    var idTeam:String? = null,

    @SerializedName("strTeam")
    var strTeam:String? = null,

    @SerializedName("strDescriptionEN")
    var description:String? = null,

    @SerializedName("strTeamBadge")
    var teamBadge:String? = null
)