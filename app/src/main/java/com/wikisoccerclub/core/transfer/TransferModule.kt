package com.wikisoccerclub.core.transfer

import com.wikisoccerclub.data.transfer.*

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

    val workflow: TransferWorkflowService by lazy {
        TransferWorkflowService(
            offerRepository = offers,
            contractRepository = contracts,
            clubRepository = clubs,
            historyRepository = history,
            windowRepository = windows
        )
    }
}
