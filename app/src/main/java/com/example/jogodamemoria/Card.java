package com.example.jogodamemoria;

// esta classe representa uma única carta do jogo
public class Card {
    private final int imageResId; // guarda o id da imagem (ex: R.drawable.card_0)
    private boolean isFaceUp = false; // controla se a carta está virada para cima
    private boolean isMatched = false; // controla se a carta já formou um par

    public Card(int imageResId, boolean isFaceUp) {
        this.imageResId = imageResId;
        this.isFaceUp = isFaceUp;
    }

    // getters (métodos para pegar os valores)
    public int getImageResId() {
        return imageResId;
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public boolean isMatched() {
        return isMatched;
    }

    // setters (métodos para definir os valores)
    public void setFaceUp(boolean faceUp) {
        isFaceUp = faceUp;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }
}