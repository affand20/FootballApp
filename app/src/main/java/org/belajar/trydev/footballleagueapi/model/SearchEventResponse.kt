package org.belajar.trydev.footballleagueapi.model

import com.google.gson.annotations.SerializedName

data class SearchEventResponse(
        @SerializedName("event")
        val events:List<Event>
)