package com.wikisoccerclub.data.match

object EnergyEngine{
    fun update(players:List<PlayerCondition>,minute:Int):List<PlayerCondition>{
        return players.map{it.afterMinute(minute)}
    }

    fun attackBonus(energy:Int):Int=
        when{
            energy>=90->8
            energy>=75->5
            energy>=60->2
            energy>=45->0
            else->-5
        }
}
