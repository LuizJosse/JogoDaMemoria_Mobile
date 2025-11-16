package com.example.jogodamemoria;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jogodamemoria.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

// a mainactivity é a "tela" principal.
// ela implementa as interfaces para "ouvir" os cliques nas cartas (onclicklistener)
// e os eventos do jogo (gamelistener)
public class MainActivity extends AppCompatActivity implements MemoryCardAdapter.OnCardClickListener, MemoryGame.GameListener {

    // --- variáveis da classe ---
    private ActivityMainBinding binding; // controla os elementos do xml (activity_main.xml)
    private MemoryCardAdapter adapter; // adaptador para a lista de cartas (recyclerview)
    private MemoryGame game; // objeto que controla a lógica do jogo
    private List<Card> cards; // lista de cartas

    // handler para criar delays (ex: 1 segundo para virar cartas erradas)
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // referências para os layouts e textos
    private ConstraintLayout menuLayout;
    private ConstraintLayout gameLayout;
    private TextView attemptsTextView;
    private Button readyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // infla (carrega) o layout usando viewbinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // --- setup inicial: "linka" as variáveis com os views do xml ---
        menuLayout = binding.menuLayout.menuContainer;
        gameLayout = binding.gameLayout.gameContainer;
        attemptsTextView = binding.gameLayout.attemptsTextView;
        readyButton = binding.gameLayout.readyButton;

        // --- define o que acontece ao clicar nos botões ---
        binding.menuLayout.startButton.setOnClickListener(v -> showGame()); // botão "iniciar jogo"
        binding.menuLayout.developersButton.setOnClickListener(v -> showDevelopersDialog()); // botão "desenvolvedores"

        // botão "começar" (que aparece depois de "iniciar jogo")
        readyButton.setOnClickListener(v -> {
            readyButton.setVisibility(View.GONE); // esconde o botão "começar"
            game.startGame(); // avisa a lógica do jogo para virar as cartas
        });

        // mostra o menu inicial quando o app abre
        showMenu();
    }

    // carrega os ids das imagens (ex: R.drawable.card_0)
    private ArrayList<Integer> getCardImageIds() {
        ArrayList<Integer> cardImages = new ArrayList<>();
        cardImages.add(R.drawable.card_0);
        cardImages.add(R.drawable.card_1);
        cardImages.add(R.drawable.card_2);
        cardImages.add(R.drawable.card_3);
        cardImages.add(R.drawable.card_4);
        cardImages.add(R.drawable.card_5);
        cardImages.add(R.drawable.card_6);
        cardImages.add(R.drawable.card_7);
        return cardImages;
    }

    // prepara um novo jogo
    private void setupGame() {
        // cria uma nova instância do jogo
        game = new MemoryGame(getCardImageIds(), this);
        cards = game.getCards(); // pega as cartas embaralhadas

        // configura o recyclerview (a lista de cartas)
        adapter = new MemoryCardAdapter(this, cards, this);
        binding.gameLayout.gameRecyclerView.setAdapter(adapter);

        // cria um gridlayout customizado que não deixa rolar (scroll)
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false; // desativa o scroll vertical
            }
        };
        binding.gameLayout.gameRecyclerView.setLayoutManager(layoutManager);

        binding.gameLayout.gameRecyclerView.setHasFixedSize(true);

        // mostra o botão "começar" (as cartas ainda estão viradas para cima)
        readyButton.setVisibility(View.VISIBLE);
    }

    // mostra o layout do menu e esconde o do jogo
    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);
        gameLayout.setVisibility(View.GONE);
    }

    // mostra o layout do jogo e esconde o do menu
    private void showGame() {
        menuLayout.setVisibility(View.GONE);
        gameLayout.setVisibility(View.VISIBLE);
        setupGame(); // prepara o novo jogo
    }

    // mostra o pop-up (dialog) com os nomes dos desenvolvedores
    private void showDevelopersDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Desenvolvedores")
                .setMessage("Yan Rios Souza & Luiz José Costa e Silva Neto")
                .setPositiveButton("Fechar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // --- métodos da interface "oncardclicklistener" ---
    // chamado pelo adapter quando uma carta é clicada
    @Override
    public void onCardClicked(int position) {
        game.onCardClicked(position); // repassa o clique para a lógica do jogo
    }

    // --- métodos da interface "gamelistener" ---
    // o memorygame chama esses métodos para avisar a mainactivity

    @Override
    public void onCardFlipped(int position) {
        adapter.notifyItemChanged(position); // avisa o adapter para redesenhar a carta
    }

    @Override
    public void onMatch(int pos1, int pos2) {
        // redesenha as duas cartas que formaram par
        adapter.notifyItemChanged(pos1);
        adapter.notifyItemChanged(pos2);
    }

    @Override
    public void onMismatch(int pos1, int pos2) {
        // se errou, espera 1 segundo (1000ms) antes de virar de volta
        mainHandler.postDelayed(() -> {
            cards.get(pos1).setFaceUp(false);
            cards.get(pos2).setFaceUp(false);

            adapter.notifyItemChanged(pos1);
            adapter.notifyItemChanged(pos2);

            game.finishMismatchProcessing(); // avisa o jogo que o delay acabou
        }, 1000);
    }

    @Override
    public void onGameWon() {
        // mostra pop-up de vitória
        new MaterialAlertDialogBuilder(this)
                .setTitle("Parabéns!")
                .setMessage("Você encontrou todos os pares. Quer jogar novamente?")
                .setPositiveButton("Jogar Novamente", (dialog, which) -> {
                    showGame(); // recomeça
                })
                .setNegativeButton("Voltar ao Menu", (dialog, which) -> {
                    showMenu(); // volta pro menu
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onAttemptsChanged(int remainingAttempts) {
        // atualiza o texto de tentativas na tela
        attemptsTextView.setText("Tentativas: " + remainingAttempts);
    }

    @Override
    public void onGameOver() {
        // mostra pop-up de fim de jogo
        new MaterialAlertDialogBuilder(this)
                .setTitle("Fim de Jogo")
                .setMessage("Suas tentativas acabaram. Quer jogar novamente?")
                .setPositiveButton("Recomeçar", (dialog, which) -> {
                    showGame(); // recomeça
                })
                .setNegativeButton("Fechar", (dialog, which) -> {
                    finish(); // fecha o app
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // limpa os delays pendentes para evitar vazamento de memória
        mainHandler.removeCallbacksAndMessages(null);
    }
}