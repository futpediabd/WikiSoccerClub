# WikiSoccerClub — Parte 7, Etapa 7.4

Esta etapa adiciona o avanço real das competições:

- cálculo das tabelas da fase de grupos;
- critérios de desempate;
- classificação dos dois primeiros de cada grupo;
- confronto direto como módulo auxiliar;
- resolução das fases eliminatórias;
- placar agregado sem gol fora;
- decisão por pênaltis;
- avanço automático dos classificados;
- eliminação dos derrotados;
- definição de campeão e vice;
- consolidação das vagas da temporada seguinte;
- estrutura de estado da competição para salvamento.

Critérios aplicados na tabela:

1. pontos;
2. vitórias;
3. saldo de gols;
4. gols marcados;
5. disciplina;
6. identificador estável como desempate técnico final.

O confronto direto foi separado em um motor próprio para ser chamado quando
os clubes permanecerem empatados nos critérios anteriores.
