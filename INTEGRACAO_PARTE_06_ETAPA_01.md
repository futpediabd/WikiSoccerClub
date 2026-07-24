# Integração Parte 6 — Etapa 6.1

## Retrospecto entre clubes

Esta etapa adiciona um histórico único de confrontos alimentado automaticamente sempre que um resultado é registrado pela carreira.

### Informações calculadas

- jogos, vitórias, empates e derrotas de cada clube;
- gols marcados e sofridos;
- desempenho como mandante e visitante;
- maior vitória em casa;
- maior vitória fora;
- maior derrota em casa;
- maior derrota fora;
- partidas recentes;
- filtro opcional por competição.

### Arquivos principais

- `data/headtohead/HeadToHeadModels.kt`
- `data/headtohead/HeadToHeadRepository.kt`
- `core/headtohead/HeadToHeadService.kt`
- `core/headtohead/HeadToHeadModule.kt`

O registro usa o identificador da partida como chave, impedindo duplicação ao restaurar ou recalcular resultados.
