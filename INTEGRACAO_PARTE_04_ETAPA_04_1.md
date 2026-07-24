# Integração Parte 4 — Etapa 4.1

## Implementado

- janelas de transferências abertas e fechadas conforme a data da carreira;
- configuração independente das janelas para cada temporada;
- bloqueio real de novas propostas, contrapropostas e conclusão da transferência fora da janela;
- eventos globais de abertura e fechamento;
- integração inicial com o calendário por meio de `updateCareerDate` / `onCareerDateChanged`;
- estado da tela com data atual, próxima abertura e histórico de eventos.

## Arquivos principais

- `data/transfer/TransferWindowModels.kt`
- `data/transfer/TransferWindowEngine.kt`
- `data/transfer/TransferWindowRepository.kt`
- `core/transfer/TransferWorkflowService.kt`
- `core/transfer/TransferModule.kt`
- `ui/transfer/TransferWindowViewModel.kt`

## Integração com o calendário da carreira

Ao avançar a data do jogo, execute:

```kotlin
TransferModule.windows.updateCareerDate(CareerDate(2026, 7, 10))
```

Ou, na tela/ViewModel:

```kotlin
viewModel.onCareerDateChanged(CareerDate(2026, 7, 10))
```

O retorno contém os eventos globais disparados entre a data anterior e a nova data.
