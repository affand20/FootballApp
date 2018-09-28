package org.belajar.trydev.footballleagueapi.model

import com.google.gson.annotations.SerializedName

data class TeamDetailResponse (
        @SerializedName("teams")
        val teamDetails:List<TeamDetail>
)