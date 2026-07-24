package com.wikisoccerclub.core.transfer

import com.wikisoccerclub.data.transfer.*
import com.wikisoccerclub.core.finance.FinanceModule
import com.wikisoccerclub.data.youth.YouthAcademyRepository

/**
 * Instâncias únicas compartilhadas por todas as telas do mercado.
 * Evita que cada ViewModel crie um repositório isolado e perca as mudanças
 * realizadas nos demais módulos.
 */
object TransferModule {
    val offers: TransferOfferRepository by lazy { TransferOfferRepository() }
    val contracts: ContractRepository by lazy { ContractRepository() }
    val loans: LoanRepository by lazy { LoanRepository() }
    val clubs: ClubTransferRepository by lazy { ClubTransferRepository() }
    val history: TransferHistoryRepository by lazy { TransferHistoryRepository() }
    val ai: TransferAiRepository by lazy { TransferAiRepository() }
    val targets: TransferRepository by lazy { TransferRepository() }
    val windows: TransferWindowRepository by lazy { TransferWindowRepository() }
    val youth: YouthAcademyRepository by lazy { YouthAcademyRepository() }
    val news: TransferNewsRepository by lazy { TransferNewsRepository() }
    val finances get() = FinanceModule.repository

    val integration: TransferIntegrationService by lazy {
        TransferIntegrationService(
            financeRepository = finances,
            newsRepository = news
        )
    }

    val recruitment: AiRecruitmentService by lazy {
        AiRecruitmentService(
            aiRepository = ai,
            windowRepository = windows
        )
    }

    val lifecycle: SquadLifecycleService by lazy {
        SquadLifecycleService(
            contracts = contracts,
            youth = youth
        )
    }

    val workflow: TransferWorkflowService by lazy {
        TransferWorkflowService(
            offerRepository = offers,
            contractRepository = contracts,
            clubRepository = clubs,
            historyRepository = history,
            windowRepository = windows,
            integrationService = integration
        )
    }
}
