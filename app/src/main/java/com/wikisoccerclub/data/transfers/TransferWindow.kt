package com.wikisoccerclub.data.transfers

enum class TransferWindowStatus{
 OPEN,CLOSED
}

object TransferWindow{
 fun status(month:Int)=
   if(month==1||month==7) TransferWindowStatus.OPEN
   else TransferWindowStatus.CLOSED
}
