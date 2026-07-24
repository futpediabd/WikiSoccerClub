# Integração Parte 5 — Etapa 5.3

## Rodadas e classificação automática

Esta etapa conecta a agenda da carreira às classificações das competições.

### Inclusões

- `CareerStandingsRepository`: classificações compartilhadas e preparadas para salvamento.
- `CareerCompetitionService`: registra placares e atualiza a tabela automaticamente.
- `CompetitionRoundSummary`: resumo de partidas concluídas e pendentes da rodada.
- Atualização da classificação em jogos do usuário e jogos simulados pela IA.
- Critérios de ordenação já existentes: pontos, vitórias, saldo, gols pró e nome.
- Consulta da próxima rodada pendente.
- Detecção automática de rodada concluída.
- Integração no `CareerModule`.

### Fluxo

1. A partida é concluída pela IA ou pelo usuário.
2. O placar é salvo na agenda da carreira.
3. O resultado é aplicado à classificação da competição.
4. A tabela é reordenada.
5. O sistema verifica se toda a rodada foi concluída.
