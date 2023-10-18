package com.example.kisupercars;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class ModelosAdapter extends RecyclerView.Adapter<ModelosAdapter.ViewHolder> {

    private Context context;
    private List<Modelos> modelosList;

    public ModelosAdapter(Context context, List<Modelos> modelosList) {
        this.context = context;
        this.modelosList = modelosList;
    }

    @NonNull
    @Override
    public ModelosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.estilo_lista, parent, false);
        return new ModelosAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelosAdapter.ViewHolder holder, int position) {
        Modelos modelo = modelosList.get(position);
        // Aquí configuras los datos del CardView, como nombreMarcaText

        holder.tv.setText(modelo.getNombreModelo());

        // Carga la imagen desde la URL almacenada en Firestore
        String imagenUrl = modelo.getImagenModelo();

        Glide.with(holder.itemView.getContext())
                .load(imagenUrl)
                .centerCrop()
                .into(holder.iv);
        // ... el resto del código, como el OnClickListener

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí implementas la función que deseas ejecutar al hacer clic en el CardView
                // Por ejemplo, puedes mostrar un Toast con un mensaje:

                // Obtener una referencia a la vista a animar (el CardView)
                View animView = v.findViewById(R.id.cv);
                // Cargar la animación desde el archivo XML
                Animation anim = AnimationUtils.loadAnimation(context, R.anim.animacion_cardview);
                // Ejecutar la animación en la vista
                animView.startAnimation(anim);

                //Toast.makeText(context, modelo.getNombreModelo(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), CaracteristicasModelo.class);
                intent.putExtra("modelo", modelo.getNombreModelo());
                intent.putExtra("marca", modelo.getNombreMarca());
                v.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return modelosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.iconoCarta);
            tv = itemView.findViewById(R.id.tx_nombreModelo);
            cardView = itemView.findViewById(R.id.cv);

        }
    }
}
