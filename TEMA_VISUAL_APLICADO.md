# Tema visual aplicado

O projeto recebeu uma identidade visual inspirada na imagem enviada:

- fundo verde vibrante;
- cabeçalhos em degradê verde e azul-escuro;
- divisores e botões amarelos;
- cartões brancos com cantos arredondados;
- títulos em azul-escuro;
- textos auxiliares em verde/cinza;
- barra de status azul e barra de navegação verde.

Arquivos principais alterados:

- `ui/theme/Color.kt`
- `ui/theme/Theme.kt`
- `ui/components/WscComponents.kt`
- `ui/screens/MainMenuScreen.kt`
- `ui/screens/HomeScreen.kt`
- `res/values/colors.xml`
- `res/values/themes.xml`

As demais telas que já utilizam `WscGreen`, `WscYellow`, `WscWhiteCard` e `WscTopBar` passam a herdar automaticamente a nova paleta e o novo acabamento visual.
