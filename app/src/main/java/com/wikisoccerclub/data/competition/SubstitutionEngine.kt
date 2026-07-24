package com.wikisoccerclub.data.competition

object SubstitutionEngine{
    fun substitute(
        state:SubstitutionState,
        minute:Int,
        outPlayer:CompetitionPlayer,
        inPlayer:CompetitionPlayer
    ):SubstitutionState{
        if(state.used>=state.maxSubstitutions) return state
        if(outPlayer.id==inPlayer.id) return state
        return state.copy(
            used=state.used+1,
            substitutions=state.substitutions+MatchSubstitution(
                minute,outPlayer.id,inPlayer.id
            )
        )
    }
}
