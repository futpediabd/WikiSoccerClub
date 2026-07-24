# WikiSoccerClub

Jogo de gerenciamento de futebol para Android, desenvolvido em Kotlin com Jetpack Compose.

## Estado atual

Versão de integração: **Parte 4 • Etapa 4.1**.

O projeto já reúne a base do modo carreira, calendário, competições, elenco, finanças, categorias de base, observação de jogadores, partidas e o primeiro ciclo do mercado de transferências.

### Integração mais recente

- Sistema de janela de transferências aberta e fechada.
- Bloqueio de negociações fora da janela.
- Configuração das janelas por temporada.
- Eventos globais de abertura e fechamento.
- Integração inicial com o calendário da carreira.

A próxima etapa planejada é a **Parte 4 • Etapa 4.2**, com análise de posições carentes, busca automática de reforços e prioridades de contratação pela IA.

## Tecnologias

- Kotlin
- Android SDK 34
- Jetpack Compose
- Material 3
- Navigation Compose
- DataStore Preferences
- Java 17

## Requisitos

- Android Studio com suporte ao Android SDK 34
- JDK 17
- Gradle 8.2 ou superior
- Dispositivo ou emulador com Android 7.0 (API 24) ou superior

## Como abrir no Android Studio

1. Clone ou baixe este repositório.
2. Abra a pasta raiz `WikiSoccerClub` no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Selecione um dispositivo ou emulador.
5. Execute o módulo `app`.

## Build pelo terminal

Este pacote não contém o Gradle Wrapper. Com o Gradle instalado, execute:

```bash
gradle assembleDebug
```

O APK de depuração será gerado em:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Estrutura principal

```text
app/src/main/java/com/wikisoccerclub/
├── core/          # Regras centrais e integrações
├── data/          # Persistência, modelos e repositórios
├── domain/        # Entidades e regras de negócio
├── navigation/    # Rotas do aplicativo
└── ui/            # Telas, componentes, temas e ViewModels
```

Os arquivos `.ban` e imagens de clubes podem ser armazenados em:

```text
app/src/main/assets/teams/
```

## Documentação das etapas

Os arquivos `README_ETAPA_*.md` e `INTEGRACAO_*.md` registram a evolução do projeto. O resumo consolidado está em [`docs/STATUS_DO_PROJETO.md`](docs/STATUS_DO_PROJETO.md).

## Observações

- Não publique chaves de assinatura, senhas ou arquivos `local.properties`.
- Escudos, bandeiras e dados reais devem respeitar as permissões de uso aplicáveis.
- O projeto está em desenvolvimento e algumas integrações ainda serão concluídas nas próximas etapas.

## Integração Parte 4 — Etapa 4.2

A IA de mercado agora analisa carências do elenco, classifica prioridades, procura reforços compatíveis e só cria propostas quando a janela de transferências está aberta. Consulte `INTEGRACAO_PARTE_04_ETAPA_04_2.md`.

## Integração Parte 4 — Etapa 4.3

Gestão automática de contratos, promoção da base e venda de atletas excedentes integrada à IA de planejamento do elenco.

## Parte 5 • Etapa 5.1

O projeto agora possui calendário central da carreira, avanço diário e eventos globais sincronizados com as janelas de transferências.

## Integrações recentes
- **Parte 5 • Etapa 5.2:** agenda de competições, partidas do dia e avanço até o próximo jogo.

## Parte 5 — Etapa 5.3

Agenda, rodadas e classificação agora trabalham de forma integrada. Todo placar concluído pela IA ou pelo usuário atualiza automaticamente a tabela da competição.
