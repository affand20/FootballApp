package org.belajar.trydev.footballleagueapi.db

data class FavoriteTeam (
        val id:Long?,
        val teamId:String?,
        val teamName:String?,
        val teamBadge:String?,
        val description:String?
){
    companion object{
        const val TABLE_FAVORITE_TEAM = "TABLE_FAVORTIE_TEAM"
        const val ID = "ID_"
        const val TEAM_ID = "TEAM_ID"
        const val TEAM_NAME = "TEAM_NAME"
        const val TEAM_BADGE = "TEAM_BADGE"
        const val DESCRIPTION = "DESCRIPTION"
    }
}