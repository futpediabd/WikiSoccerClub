package com.wikisoccerclub.data.season

data class Season(val year:Int,val currentCompetitionId:String?=null,val currentRound:Int=1,val finished:Boolean=false)
data class SeasonSummary(val year:Int,val champions:Map<String,String>=emptyMap())