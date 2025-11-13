# DevClicker - The Coding Adventure

DevClicker é um jogo "idle clicker" para Android, onde você simula ser um desenvolvedor, ganhando "DevPoints" ao clicar e comprando upgrades para automatizar seu trabalho.

Este projeto foi desenvolvido como parte do Trabalho Final em Grupo – Android 2025, focado em demonstrar competências em arquitetura Android moderna.


## 🚀 Funcionalidades

* **Autenticação:** Login e Cadastro de usuários usando **Firebase Authentication**.
* **Jogo Clicker:** Clique para ganhar pontos (DevPoints).
* **Sistema de Upgrade:** Compre upgrades (Mouse Novo, Teclado Mecânico, Script Básico) que aumentam seus pontos por clique (PPC) e pontos por segundo (PPS).
* **Persistência Local:** O progresso do jogo (pontos e upgrades) é salvo localmente usando **Room Database**.
* **UI Moderna:** Interface totalmente construída com **Jetpack Compose**.

## 🏛️ Arquitetura

O projeto segue o padrão **MVVM (Model-View-ViewModel)** e os princípios de "Clean Architecture".

* **UI (Compose):** Camada de apresentação, dividida por telas (`clicker`, `upgrades`, `auth`).
* **ViewModel:** Contém a lógica de UI e o estado (`UiState`), expondo-os através de `StateFlow`.
* **Repository:** Camada de lógica de negócios que abstrai as fontes de dados.
* **Data (Room & Firebase):** Fontes de dados. Room para dados locais e Firebase para autenticação.
* **DI (Hilt):** Hilt é usado para injeção de dependência em todo o app (ViewModels, Repositories, Database).

### Estrutura de Pacotes

com.example.devclicker
│
├── data
│   ├── dao/               # Interfaces do Room (JogadorDao, UpgradeDao)
│   ├── database/          # Definição do AppDatabase
│   ├── model/             # Entidades do Room (Jogador, UpgradeComprado)
│   ├── repository/        # Lógica de negócios (GameRepository, AuthRepository)
│   └── di/                # Módulo do Hilt (HiltModule.kt)
│
├── ui
│   ├── auth/              # Telas de Login e Cadastro
│   ├── game/              # Telas do Jogo (Clicker, Upgrades, Settings)
│   ├── navigation/        # Lógica de navegação (NavGraphs, BottomNav)
│   └── theme/             # Definições de tema do Jetpack Compose
│
├── MainActivity.kt        # Rota principal com o AppNavigation
└── MainApplication.kt     # Ponto de entrada do Hilt


## 🛠️ Como Rodar o Projeto

### Pré-requisitos

1.  Android Studio (versão mais recente, ex: Koala ou posterior)
2.  JDK 11 ou superior

### Configuração do Firebase

Este projeto requer uma configuração do Firebase para funcionar.

1.  Vá até o [Console do Firebase](https://console.firebase.google.com/).
2.  Crie um novo projeto.
3.  Adicione um aplicativo Android com o nome de pacote `com.example.devclicker`.
4.  **Habilite** o serviço de **Authentication** (com E-mail/Senha).
5.  Baixe o arquivo `google-services.json` e coloque-o na pasta `app/`.

### Build

1.  Clone o repositório:
    ```bash
    git clone [https://github.com/lukkasdanielch/DevClicker.git](https://github.com/lukkasdanielch/DevClicker.git)
    ```
2.  Abra o projeto no Android Studio.
3.  O Gradle irá sincronizar as dependências.
4.  Execute o app em um emulador ou dispositivo físico (API 26+).

## 🧪 Como Testar

O projeto está configurado para testes unitários e instrumentados, mas a cobertura atual é baixa (foco do trabalho foi na implementação da arquitetura).

* **Testes Unitários:**
    ```bash
    ./gradlew testDebugUnitTest
    ```
* **Testes Instrumentados:**
    ```bash
    ./gradlew connectedAndroidTest
    ```
