package com.wikisoccerclub.data.transfer

class TransferRepository{
    private val targets=mutableListOf<TransferTarget>()
    fun save(target:TransferTarget){targets.removeAll{it.playerId==target.playerId};targets+=target}
    fun all()=targets.toList()
    fun remove(playerId:String){targets.removeAll{it.playerId==playerId}}
}
