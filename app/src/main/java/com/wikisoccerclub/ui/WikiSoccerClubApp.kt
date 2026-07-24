package com.wikisoccerclub.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wikisoccerclub.navigation.AppRoutes
import com.wikisoccerclub.ui.competition.CompetitionViewModel
import com.wikisoccerclub.ui.transfer.LoanContractViewModel
import com.wikisoccerclub.ui.transfer.TransferAiViewModel
import com.wikisoccerclub.ui.transfer.TransferCompletionViewModel
import com.wikisoccerclub.ui.transfer.TransferOfferViewModel
import com.wikisoccerclub.ui.transfer.TransferWindowViewModel
import com.wikisoccerclub.ui.screens.CompetitionHubScreen
import com.wikisoccerclub.ui.screens.ContractNegotiationScreen
import com.wikisoccerclub.ui.screens.LoanOffersScreen
import com.wikisoccerclub.ui.screens.TransferAiMarketScreen
import com.wikisoccerclub.ui.screens.TransferHistoryScreen
import com.wikisoccerclub.ui.screens.TransferMarketHubScreen
import com.wikisoccerclub.ui.screens.TransferOffersScreen
import com.wikisoccerclub.ui.screens.TransferWindowScreen
import com.wikisoccerclub.ui.screens.CreditsScreen
import com.wikisoccerclub.ui.screens.CurrentRoundScreen
import com.wikisoccerclub.ui.screens.EditorScreen
import com.wikisoccerclub.ui.screens.FixturesScreen
import com.wikisoccerclub.ui.screens.HomeScreen
import com.wikisoccerclub.ui.screens.LineupScreen
import com.wikisoccerclub.ui.screens.LoadGameScreen
import com.wikisoccerclub.ui.screens.MainMenuScreen
import com.wikisoccerclub.ui.screens.MatchScreen
import com.wikisoccerclub.ui.screens.NewGameScreen
import com.wikisoccerclub.ui.screens.SquadScreen
import com.wikisoccerclub.ui.screens.StandingsScreen
import com.wikisoccerclub.ui.screens.TopScorersScreen

@Composable
fun WikiSoccerClubApp(
    appViewModel: AppViewModel = viewModel(),
    competitionViewModel: CompetitionViewModel = viewModel(),
    transferOfferViewModel: TransferOfferViewModel = viewModel(),
    loanContractViewModel: LoanContractViewModel = viewModel(),
    transferAiViewModel: TransferAiViewModel = viewModel(),
    transferCompletionViewModel: TransferCompletionViewModel = viewModel(),
    transferWindowViewModel: TransferWindowViewModel = viewModel()
) {
    val navController = rememberNavController()
    val competitionState = competitionViewModel.uiState.collectAsState()
    val transferOfferState = transferOfferViewModel.ui.collectAsState()
    val loanContractState = loanContractViewModel.uiState.collectAsState()
    val transferAiState = transferAiViewModel.uiState.collectAsState()
    val transferCompletionState = transferCompletionViewModel.uiState.collectAsState()
    val transferWindowState = transferWindowViewModel.ui.collectAsState()
    val playerNames = appViewModel.currentSquad().associate { it.id to it.name }
    val clubNames = appViewModel.clubs.flatMap { club ->
        listOf(club.sourceFile to club.name, club.name to club.name)
    }.toMap()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.MENU
    ) {
        composable(AppRoutes.MENU) {
            MainMenuScreen(
                onNewGame = { navController.navigate(AppRoutes.NEW_GAME) },
                onLoadGame = { navController.navigate(AppRoutes.LOAD_GAME) },
                onEditor = { navController.navigate(AppRoutes.EDITOR) },
                onCredits = { navController.navigate(AppRoutes.CREDITS) }
            )
        }

        composable(AppRoutes.NEW_GAME) {
            NewGameScreen(
                clubs = appViewModel.clubs,
                onBack = { navController.popBackStack() },
                onCreateGame = { manager, club ->
                    appViewModel.createGame(manager, club) {
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.MENU)
                        }
                    }
                }
            )
        }

        composable(AppRoutes.LOAD_GAME) {
            LoadGameScreen(
                savedGame = appViewModel.savedGame,
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate(AppRoutes.HOME) }
            )
        }

        composable(AppRoutes.EDITOR) {
            EditorScreen(appViewModel.clubs) {
                navController.popBackStack()
            }
        }

        composable(AppRoutes.CREDITS) {
            CreditsScreen {
                navController.popBackStack()
            }
        }

        composable(AppRoutes.HOME) {
            HomeScreen(
                savedGame = appViewModel.savedGame,
                club = appViewModel.currentClub(),
                nextMatch = appViewModel.nextMatch(),
                onCompetition = {
                    navController.navigate(AppRoutes.COMPETITION)
                },
                onSquad = {
                    navController.navigate(AppRoutes.SQUAD)
                },
                onTransferMarket = {
                    navController.navigate(AppRoutes.TRANSFER_MARKET)
                },
                onNextMatch = {
                    navController.navigate(AppRoutes.LINEUP)
                },
                onBackToMenu = {
                    navController.navigate(AppRoutes.MENU) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(AppRoutes.COMPETITION) {
            val state = competitionState.value
            CompetitionHubScreen(
                competitionName = state.competitionName,
                currentRound = state.currentRound,
                totalRounds = state.totalRounds,
                completed = state.completed,
                onOpenCurrentRound = {
                    navController.navigate(AppRoutes.CURRENT_ROUND)
                },
                onOpenStandings = {
                    navController.navigate(AppRoutes.STANDINGS)
                },
                onOpenFixtures = {
                    navController.navigate(AppRoutes.FIXTURES)
                },
                onOpenTopScorers = {
                    navController.navigate(AppRoutes.TOP_SCORERS)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.CURRENT_ROUND) {
            val state = competitionState.value
            CurrentRoundScreen(
                round = state.currentRound,
                teams = state.teams,
                matches = state.currentRoundMatches,
                onSimulateRound = {
                    competitionViewModel.simulateCurrentRound()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.STANDINGS) {
            val state = competitionState.value
            StandingsScreen(
                competitionName = state.competitionName,
                standings = state.standings,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.TOP_SCORERS) {
            val state = competitionState.value
            TopScorersScreen(
                competitionName = state.competitionName,
                scorers = state.topScorers,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.FIXTURES) {
            val state = competitionState.value
            FixturesScreen(
                competitionName = state.competitionName,
                teams = state.teams,
                matches = state.matches,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.SQUAD) {
            SquadScreen(
                players = appViewModel.currentSquad(),
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.LINEUP) {
            LineupScreen(
                match = appViewModel.nextMatch(),
                lineup = appViewModel.automaticLineup(),
                onBack = {
                    navController.popBackStack()
                },
                onPlay = { lineup ->
                    appViewModel.playNextMatch(lineup) {
                        navController.navigate(AppRoutes.MATCH)
                    }
                }
            )
        }

        composable(AppRoutes.MATCH) {
            MatchScreen(
                match = appViewModel.nextMatch(),
                result = appViewModel.lastMatchResult,
                onClose = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.HOME) {
                            inclusive = true
                        }
                    }
                }
            )
        }


        composable(AppRoutes.TRANSFER_MARKET) {
            TransferMarketHubScreen(
                onOpenWindow = { navController.navigate(AppRoutes.TRANSFER_WINDOW) },
                onOpenOffers = { navController.navigate(AppRoutes.TRANSFER_OFFERS) },
                onOpenContracts = { navController.navigate(AppRoutes.TRANSFER_CONTRACTS) },
                onOpenLoans = { navController.navigate(AppRoutes.TRANSFER_LOANS) },
                onOpenAiMarket = { navController.navigate(AppRoutes.TRANSFER_AI) },
                onOpenHistory = { navController.navigate(AppRoutes.TRANSFER_HISTORY) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.TRANSFER_WINDOW) {
            LaunchedEffect(Unit) { transferWindowViewModel.load(appViewModel.savedGame.value?.season ?: 2026) }
            TransferWindowScreen(
                windows = transferWindowState.value.windows,
                targets = transferWindowState.value.targets,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.TRANSFER_OFFERS) {
            LaunchedEffect(Unit) { transferOfferViewModel.load() }
            TransferOffersScreen(
                offers = transferOfferState.value.offers,
                playerNames = playerNames,
                clubNames = clubNames,
                error = transferOfferState.value.error,
                onAccept = transferOfferViewModel::accept,
                onReject = transferOfferViewModel::reject,
                onComplete = { offerId ->
                    transferCompletionViewModel.completeAcceptedOffer(
                        offerId,
                        appViewModel.savedGame.value?.season ?: 2026
                    )
                    transferOfferViewModel.load()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.TRANSFER_CONTRACTS) {
            LaunchedEffect(Unit) { loanContractViewModel.load() }
            ContractNegotiationScreen(
                offers = loanContractState.value.contractOffers,
                playerNames = playerNames,
                onAccept = loanContractViewModel::acceptContract,
                onReject = loanContractViewModel::rejectContract,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.TRANSFER_LOANS) {
            LaunchedEffect(Unit) { loanContractViewModel.load() }
            LoanOffersScreen(
                offers = loanContractState.value.loanOffers,
                playerNames = playerNames,
                clubNames = clubNames,
                onAccept = loanContractViewModel::acceptLoan,
                onReject = loanContractViewModel::rejectLoan,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.TRANSFER_AI) {
            TransferAiMarketScreen(
                decisions = transferAiState.value.decisions,
                processing = transferAiState.value.processing,
                error = transferAiState.value.error,
                playerNames = playerNames,
                clubNames = clubNames,
                onSimulate = { transferAiViewModel.simulateMarket() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.TRANSFER_HISTORY) {
            LaunchedEffect(Unit) { transferCompletionViewModel.load() }
            TransferHistoryScreen(
                transfers = transferCompletionState.value.transfers,
                playerNames = playerNames,
                clubNames = clubNames,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
