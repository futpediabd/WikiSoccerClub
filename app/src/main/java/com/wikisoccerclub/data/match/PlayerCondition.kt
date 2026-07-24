package com.wikisoccerclub.data.match

data class PlayerCondition(
    val playerId:String,
    val energy:Int=100,
    val morale:Int=80
){
    fun afterMinute(minute:Int):PlayerCondition{
        val loss=(minute/6).coerceAtMost(35)
        return copy(energy=(100-loss).coerceAtLeast(45))
    }
}
