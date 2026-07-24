package com.wikisoccerclub.data.transfer

enum class OfferStatus { PENDING, ACCEPTED, REJECTED, COUNTER }

data class TransferOffer(
    val id:String,
    val playerId:String,
    val sellingClubId:String,
    val buyingClubId:String,
    val value:Long,
    val installmentValue:Long=0,
    val installments:Int=0,
    val status:OfferStatus=OfferStatus.PENDING
)
