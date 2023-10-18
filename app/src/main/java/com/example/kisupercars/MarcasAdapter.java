package com.example.kisupercars;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class MarcasAdapter extends RecyclerView.Adapter<MarcasAdapter.ViewHolder> {

    private Context context;
    private List<Marcas> marcasList;


    public MarcasAdapter(Context context, List<Marcas> marcasList) {
        this.context = context;
        this.marcasList = marcasList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_marcas, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Marcas marca = marcasList.get(position);
        // Aquí configuras los datos del CardView, como nombreMarcaText

        // Carga la imagen desde la URL almacenada en Firestore
        String imagenUrl = marca.getImagenUrl();

        Glide.with(holder.itemView.getContext())
                .load(imagenUrl)
                .centerCrop()
                .into(holder.iv);
        // ... el resto del código, como el OnClickListener

        holder.itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Obtén el ancho del CardView
                int cardViewWidth = holder.itemView.getWidth();

                // Asigna la altura del CardView igual al ancho
                ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
                layoutParams.height = cardViewWidth;
                holder.itemView.setLayoutParams(layoutParams);

                // Quita el listener
                holder.itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí implementas la función que deseas ejecutar al hacer clic en el CardView
                // Por ejemplo, puedes mostrar un Toast con un mensaje:

                // Obtener una referencia a la vista a animar (el CardView)
                View animView = v.findViewById(R.id.cardViewMarcas);
                // Cargar la animación desde el archivo XML
                Animation anim = AnimationUtils.loadAnimation(context, R.anim.animacion_cardview);
                // Ejecutar la animación en la vista
                animView.startAnimation(anim);

                //Toast.makeText(context, marca.getNombreMarca(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), ElegirModelo.class);
                intent.putExtra("marca", marca.getNombreMarca());
                v.getContext().startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return marcasList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.imagenCardView);
            cardView = itemView.findViewById(R.id.cardViewMarcas);

        }
    }
}
