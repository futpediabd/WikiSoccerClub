package com.wikisoccerclub.core.headtohead

import com.wikisoccerclub.data.headtohead.HeadToHeadRepository

object HeadToHeadModule {
    val repository: HeadToHeadRepository by lazy { HeadToHeadRepository() }
    val service: HeadToHeadService by lazy { HeadToHeadService(repository) }
}
