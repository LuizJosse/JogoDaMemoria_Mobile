package com.example.jogodamemoria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

// o adapter é a "ponte" entre a lista de dados (cards) e o recyclerview (a grade visual)
public class MemoryCardAdapter extends RecyclerView.Adapter<MemoryCardAdapter.ViewHolder> {

    private Context context;
    private List<Card> cards;
    private OnCardClickListener clickListener; // referência para a mainactivity

    // interface para comunicar o clique
    public interface OnCardClickListener {
        void onCardClicked(int position);
    }

    public MemoryCardAdapter(Context context, List<Card> cards, OnCardClickListener clickListener) {
        this.context = context;
        this.cards = cards;
        this.clickListener = clickListener;
    }

    // chamado quando o recyclerview precisa criar um novo item visual (viewholder)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    // chamado quando o recyclerview precisa "ligar" os dados (card) a um item visual (holder)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(cards.get(position)); // chama o método bind do viewholder
    }

    // informa quantos itens existem na lista
    @Override
    public int getItemCount() {
        return cards.size();
    }

    // o viewholder "segura" as referências para os views de cada item (cada carta)
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardFront; // imagem da frente (o desenho)
        ImageView cardBack; // imagem de trás (a interrogação)
        MaterialCardView cardView; // o container da carta

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // "linka" as variáveis com os views do card_item.xml
            cardFront = itemView.findViewById(R.id.card_front_view);
            cardBack = itemView.findViewById(R.id.card_back_view);
            cardView = itemView.findViewById(R.id.card_view_root);

            // define o listener de clique aqui (é mais eficiente que no "bind")
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    int position = getAdapterPosition(); // pega a posição do item clicado
                    if (position != RecyclerView.NO_POSITION) {
                        // avisa a mainactivity que esta posição foi clicada
                        clickListener.onCardClicked(position);
                    }
                }
            });
        }

        // atualiza o visual da carta com base nos dados do objeto "card"
        public void bind(Card card) {
            cardFront.setImageResource(card.getImageResId());

            // decide qual lado da carta mostrar
            if (card.isFaceUp() || card.isMatched()) {
                cardFront.setVisibility(View.VISIBLE);
                cardBack.setVisibility(View.INVISIBLE);
            } else {
                cardFront.setVisibility(View.INVISIBLE);
                cardBack.setVisibility(View.VISIBLE);
            }

            // deixa a carta meio transparente se já for um par
            if (card.isMatched()) {
                cardView.setAlpha(0.4f);
            } else {
                cardView.setAlpha(1.0f);
            }
        }
    }
}