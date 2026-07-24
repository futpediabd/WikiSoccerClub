# Contribuindo

## Fluxo recomendado

1. Crie uma branch a partir de `main`.
2. Faça alterações pequenas e relacionadas a uma única etapa.
3. Confirme que o projeto sincroniza e compila.
4. Atualize a documentação da etapa correspondente.
5. Abra um Pull Request descrevendo arquivos alterados, comportamento novo e testes realizados.

## Padrões

- Use Kotlin e Jetpack Compose.
- Preserve o pacote base `com.wikisoccerclub`.
- Mantenha regras de negócio fora das telas sempre que possível.
- Não inclua segredos, arquivos de assinatura ou `local.properties`.
- Evite mudanças não relacionadas no mesmo commit.
