# SpaceXcavers – Como rodar o jogo

Réplica de Space Invaders feita em Java com o framework [FXGL](https://github.com/AlmasB/FXGL) (versão 17.3) e JavaFX (versão 21.0.6).

## 1. Pré-requisitos

-   **JDK 21 ou superior** instalado.
    -   O `pom.xml` compila com `release 21`, então JDK 21 é o mínimo.
-   **Conexão com a internet** na primeira execução, para o Maven baixar as dependências.

## 2. Estrutura do projeto

```
spacexcavers/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .mvn/
└── src/
    └── main/
        ├── java/com/juridico/
        │   ├── SpaceXcaversApp.java   <- classe principal
        │   ├── NovoMenu.java
        │   ├── Entidades.java
        │   ├── InimigoMovimentacao.java
        │   └── EstrelaMovimenta.java
        └── resources/
            └── com/juridico/spacexcavers/

```

A classe principal é `com.juridico.SpaceXcaversApp`.

## 3. Executando o jogo

Abra um terminal **dentro da pasta `spacexcavers`** (a pasta que contém o `pom.xml`).

### Linux / macOS

```bash
./mvnw clean javafx:run

```


### Windows

```bat
mvnw.cmd clean javafx:run

```

### Com Maven instalado globalmente

Se você já tem o Maven instalado, pode usar:

```bash
mvn clean javafx:run

```

O comando `javafx:run` compila o projeto e abre a janela do jogo. Na primeira vez o download das dependências pode demorar alguns minutos.


## 4. Controles

Tecla

Ação

`A` / `←`

Move o jogador para a esquerda

`D` / `→`

Move o jogador para a direita

`Espaço`

Atira

`P`

Abre o menu do jogo (Continuar / Sair)

## 5. Objetivo

-   Destrua a formação de inimigos antes que eles acabem com as suas vidas.
-   Os inimigos aceleram conforme são destruídos.
-   Ao vencer ou perder, uma caixa de diálogo pergunta se você quer jogar de novo.
-   Estrelas cadentes dão um power-up temporário (tiro mais forte e movimentação mais rápida).
