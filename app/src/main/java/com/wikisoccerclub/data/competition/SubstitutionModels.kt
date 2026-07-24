package com.wikisoccerclub.data.competition

data class MatchSubstitution(
    val minute:Int,
    val playerOutId:String,
    val playerInId:String
)

data class SubstitutionState(
    val maxSubstitutions:Int=5,
    val used:Int=0,
    val substitutions:List<MatchSubstitution> = emptyList()
){
    val remaining:Int get() = maxSubstitutions-used
}
