# Integração Parte 4 — Etapa 1 corrigida

Esta versão usa somente classes que já existem no projeto.

## Alterações reais

- Criado `TransferModule` com instâncias únicas dos repositórios existentes.
- Criado `TransferWorkflowService` para ligar proposta, contrato, conclusão,
  atualização dos clubes e histórico.
- `TransferOfferViewModel` agora aceita, rejeita e contrapropõe pelo fluxo comum.
- `LoanContractViewModel` compartilha contratos e empréstimos com as outras telas.
- `TransferCompletionViewModel` compartilha clubes e histórico e pode concluir
  diretamente uma proposta aceita.
- `TransferAiViewModel` salva as propostas da IA no mesmo repositório exibido
  pelo mercado.
- `TransferWindowViewModel` usa a mesma lista compartilhada de alvos.

Com isso, os módulos de transferências deixam de criar bancos temporários
independentes a cada tela.
