package com.wikisoccerclub.data.season

object SeasonEngine{fun next(current:Season)=current.copy(year=current.year+1,currentCompetitionId=null,currentRound=1,finished=false)}