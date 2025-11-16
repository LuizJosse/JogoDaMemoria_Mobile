package com.example.jogodamemoria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// esta classe cuida de toda a lógica e regras do jogo
public class MemoryGame {

    // interface para "avisar" a mainactivity sobre o que acontece no jogo
    public interface GameListener {
        void onCardFlipped(int position); // avisa que uma carta virou
        void onMatch(int pos1, int pos2); // avisa que deu par
        void onMismatch(int pos1, int pos2); // avisa que não deu par
        void onGameWon(); // avisa que o jogo foi ganho
        void onAttemptsChanged(int remainingAttempts); // avisa que as tentativas mudaram
        void onGameOver(); // avisa que as tentativas acabaram
    }

    // --- variáveis da classe ---
    private final List<Card> cards; // a lista de todas as cartas
    private final GameListener listener; // o "ouvinte" (a mainactivity)
    private final int totalPairs; // total de pares (ex: 8)

    private Card firstCard = null; // guarda a primeira carta clicada
    private int firstCardPosition = -1; // guarda a posição da primeira carta
    private boolean isProcessing = false; // bloqueia cliques enquanto checa um par
    private int pairsFound = 0; // contador de pares encontrados
    private boolean isGameWon = false; // controla se o jogo terminou
    private int remainingAttempts; // contador de tentativas restantes
    private static final int MAX_ATTEMPTS = 5; // define o limite de tentativas

    // construtor, prepara o jogo quando é chamado
    public MemoryGame(List<Integer> uniqueImageIds, GameListener listener) {
        this.listener = listener;
        this.totalPairs = uniqueImageIds.size();
        this.cards = new ArrayList<>();
        this.remainingAttempts = MAX_ATTEMPTS;

        // duplica as imagens para formar os pares
        List<Integer> fullDeckImages = new ArrayList<>(uniqueImageIds);
        fullDeckImages.addAll(uniqueImageIds);
        Collections.shuffle(fullDeckImages); // embaralha as imagens

        // cria as cartas, começando viradas para cima (para o preview)
        for (int imageId : fullDeckImages) {
            cards.add(new Card(imageId, true));
        }
        listener.onAttemptsChanged(remainingAttempts); // atualiza o texto de tentativas na tela
    }

    // --- métodos públicos (o que a mainactivity pode chamar) ---

    public List<Card> getCards() {
        return cards;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    // vira todas as cartas para baixo quando o jogo começa de verdade
    public void startGame() {
        isProcessing = true;
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setFaceUp(false);
            listener.onCardFlipped(i); // avisa a ui para atualizar (virar)
        }
        isProcessing = false; // libera os cliques
    }

    // processa o clique em uma carta
    public void onCardClicked(int position) {
        // ignora o clique se estiver processando, o jogo acabou ou não tem tentativas
        if (isProcessing || isGameWon || remainingAttempts <= 0) return;

        Card clickedCard = cards.get(position);
        // ignora o clique se a carta já estiver virada ou for um par
        if (clickedCard.isFaceUp() || clickedCard.isMatched()) {
            return;
        }

        isProcessing = true; // bloqueia cliques
        flipCard(clickedCard, position);
    }

    // avisa que o delay de "errou" acabou e pode processar de novo
    public void finishMismatchProcessing() {
        isProcessing = false;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    // --- métodos privados (lógica interna) ---

    // vira a carta
    private void flipCard(Card card, int position) {
        card.setFaceUp(true);
        listener.onCardFlipped(position); // avisa a ui

        if (firstCard == null) {
            // é a primeira carta da rodada
            firstCard = card;
            firstCardPosition = position;
            isProcessing = false; // libera o clique para a segunda carta
        } else {
            // é a segunda carta, checa se forma par
            checkForMatch(firstCard, firstCardPosition, card, position);
        }
    }

    // checa se as duas cartas são iguais
    private void checkForMatch(Card card1, int pos1, Card card2, int pos2) {
        if (card1.getImageResId() == card2.getImageResId()) {
            // deu par
            card1.setMatched(true);
            card2.setMatched(true);
            pairsFound++;
            listener.onMatch(pos1, pos2);

            if (pairsFound == totalPairs) {
                isGameWon = true;
                listener.onGameWon(); // avisa que ganhou
            }
            isProcessing = false; // libera cliques
        } else {
            // não deu par
            remainingAttempts--;
            listener.onAttemptsChanged(remainingAttempts); // atualiza tentativas
            if (remainingAttempts <= 0) {
                listener.onGameOver(); // avisa que perdeu
            }
            listener.onMismatch(pos1, pos2); // avisa que errou
        }

        // reseta para a próxima jogada
        firstCard = null;
        firstCardPosition = -1;
    }
}