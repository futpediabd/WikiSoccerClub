package com.wikisoccerclub.ui.season

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.season.*
import kotlinx.coroutines.flow.*

data class SeasonUiState(val season:Season)
class SeasonViewModel(private val repo:SeasonRepository=SeasonRepository()):ViewModel(){private val _ui=MutableStateFlow(SeasonUiState(repo.current()));val ui:StateFlow<SeasonUiState>=_ui.asStateFlow();fun nextSeason(){val n=SeasonEngine.next(repo.current());repo.save(n);_ui.value=SeasonUiState(n)};fun saveSummary(s:SeasonSummary)=repo.addSummary(s)}