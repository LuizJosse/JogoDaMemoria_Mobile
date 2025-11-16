📘 README — Jogo da Memória (Android / Java)
🧠 Jogo da Memória

Este projeto é um Jogo da Memória desenvolvido em Android Studio utilizando Java, RecyclerView e ViewBinding.
O objetivo é encontrar todos os pares de cartas com o menor número possível de tentativas.

🎮 Funcionalidades

✔ Virar cartas e verificar pares
✔ Embaralhamento automático a cada partida
✔ Delay para virar cartas erradas (efeito visual)
✔ Contador de tentativas
✔ Botão para iniciar uma nova rodada
✔ Interface simples e responsiva
✔ Uso de RecyclerView para renderizar a grade de cartas
✔ Organização em classes (POO)

🏗 Estrutura do Projeto
src/main/java/com.example.jogodamemoria/
│── Card.java              # Representa uma carta individual
│── MemoryGame.java        # Lógica principal do jogo
│── MemoryCardAdapter.java # Adapter do RecyclerView
│── MainActivity.java      # Tela principal / controle da UI

src/main/res/
│── drawable/              # Imagens das cartas
│── layout/activity_main.xml
│── values/colors.xml, strings.xml, themes.xml

🔧 Tecnologias utilizadas

Java

Android Studio

ViewBinding

RecyclerView

ConstraintLayout

Handlers / Loopers (delay para virar cartas)

▶ Como rodar o projeto

Abra o Android Studio

Clique em File > Open

Selecione a pasta do projeto

Aguarde o Gradle sincronizar

Execute o app em um emulador ou dispositivo físico

📱 Tela do Jogo (exemplo)
[ Grade 4x3 ou 4x4 de cartas ]
[ Contador de tentativas ]
[ Botão de "Pronto" ou "Nova Partida" ]

📌 Conceitos aplicados (POO)
Encapsulamento
Cada objeto “Card” armazena seu próprio estado (imagem, se está virado ou não).

Abstração
A classe MemoryGame concentra toda a lógica da partida.

Herança
Utilização das classes padrão do Android (AppCompatActivity, RecyclerView.Adapter).

Polimorfismo
Sobrescrita de métodos do Adapter e Activity.

💡 Melhorias futuras
Tela final de vitória
Animações ao virar as cartas
Dificuldade crescente (mais cartas)
Sistema de recorde
