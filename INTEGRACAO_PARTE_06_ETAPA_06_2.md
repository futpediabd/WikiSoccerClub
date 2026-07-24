# Integração Parte 6 — Etapa 6.2

## Sequências históricas e recordes dos clubes

Esta etapa adiciona cálculo automático de:

- invencibilidade;
- vitórias consecutivas;
- derrotas consecutivas;
- jogos sem vencer;
- partidas sem sofrer gols;
- partidas consecutivas marcando gols;
- sequência atual e maior sequência histórica;
- filtro opcional por competição;
- indicação se o recorde permanece ativo.

## Novos arquivos

- `data/records/ClubStreakModels.kt`
- `core/records/ClubStreakService.kt`
- `core/records/RecordsModule.kt`

Os cálculos usam o mesmo histórico alimentado automaticamente pelo módulo de retrospecto da Etapa 6.1.
