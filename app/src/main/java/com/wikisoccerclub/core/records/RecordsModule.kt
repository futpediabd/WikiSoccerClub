package com.wikisoccerclub.core.records

import com.wikisoccerclub.core.headtohead.HeadToHeadModule

object RecordsModule {
    val clubStreakService: ClubStreakService by lazy {
        ClubStreakService(HeadToHeadModule.repository)
    }
}
