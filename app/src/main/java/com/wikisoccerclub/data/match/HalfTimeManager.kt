package com.wikisoccerclub.data.match

object HalfTimeManager{
    fun mustOpenSubstitutionWindow(minute:Int):Boolean{
        return minute==45
    }
}
