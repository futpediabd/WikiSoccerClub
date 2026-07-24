package com.wikisoccerclub.data.transfer

object TransferOfferEngine{
    fun accept(o:TransferOffer)=o.copy(status=OfferStatus.ACCEPTED)
    fun reject(o:TransferOffer)=o.copy(status=OfferStatus.REJECTED)
    fun counter(o:TransferOffer,newValue:Long)=o.copy(value=newValue,status=OfferStatus.COUNTER)
}
