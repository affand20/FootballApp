package org.belajar.trydev.footballleagueapi.model

import com.google.gson.annotations.SerializedName

data class Player (
        @SerializedName("strPlayer")
        var strPlayer:String? = null,

        @SerializedName("strDescriptionEN")
        var description:String? = null,

        @SerializedName("strPosition")
        var position:String? = null,

        @SerializedName("strCutout")
        var photo:String? = null,

        @SerializedName("strFanart1")
        var banner:String? = null
)