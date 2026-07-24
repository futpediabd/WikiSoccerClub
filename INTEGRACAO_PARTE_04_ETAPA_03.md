# Integração Parte 4 — Etapa 3

## Salvamento do mercado

O `GameSave` agora guarda:

- propostas de compra e contrapropostas;
- propostas de contrato;
- contratos assinados;
- propostas e contratos de empréstimo;
- elencos e saldos usados pelo mercado;
- histórico de transferências concluídas.

## Arquivos principais

- `data/save/TransferSaveState.kt`
- `data/save/TransferSaveCodec.kt`
- `core/transfer/TransferSaveBridge.kt`
- `data/save/GameSave.kt`
- `data/save/GameSaveRepository.kt`

## Uso

Antes de salvar:
`gameSave.copy(transferState = TransferSaveBridge.snapshot())`

Depois de carregar:
`TransferSaveBridge.restore(gameSave.transferState)`

Ao apagar a carreira:
`TransferSaveBridge.clear()`

O formato usa o DataStore já existente e não adiciona biblioteca externa.
