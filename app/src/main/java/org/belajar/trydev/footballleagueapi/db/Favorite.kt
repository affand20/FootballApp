package org.belajar.trydev.footballleagueapi.db

data class Favorite(
        val id:Long?,
        val eventId:String?,
        val eventName:String?,
        val dateEvent:String?,
        val homeTeamId:String?,
        val awayTeamId:String?,
        val homeTeamName:String?,
        val awayTeamName:String?,
        val homeScore:String?,
        val awayScore:String?,
        val homeShots:String?,
        val awayShots:String?
        ) {
    companion object {
        const val TABLE_FAVORITE:String = "TABLE_FAVORITE"
        const val ID:String = "ID_"
        const val EVENT_ID:String = "EVENT_ID"
        const val EVENT_NAME:String = "EVENT_NAME"
        const val DATE_EVENT:String = "DATE_EVENT"
        const val HOME_TEAM_ID:String = "HOME_TEAM_ID"
        const val AWAY_TEAM_ID:String = "AWAY_TEAM_ID"
        const val HOME_TEAM_NAME:String = "HOME_TEAM_NAME"
        const val AWAY_TEAM_NAME:String = "AWAY_TEAM_NAME"
        const val HOME_SCORE:String = "HOME_SCORE"
        const val AWAY_SCORE:String = "AWAY_SCORE"
        const val HOME_SHOTS:String = "HOME_SHOTS"
        const val AWAY_SHOTS:String = "AWAY_SHOTS"
    }
}