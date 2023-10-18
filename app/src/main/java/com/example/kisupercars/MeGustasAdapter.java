package com.example.kisupercars;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MeGustasAdapter extends RecyclerView.Adapter<MeGustasAdapter.ViewHolder>{
    private Context context;
    private List<Modelos> modelosList;


    public MeGustasAdapter(Context context, List<Modelos> modelosList) {
        this.context = context;
        this.modelosList = modelosList;
    }

    @NonNull
    @Override
    public MeGustasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.estilo_modelos_megustas, parent, false);
        return new MeGustasAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MeGustasAdapter.ViewHolder holder, int position) {
        Modelos modelo = modelosList.get(position);
        // Aquí configuras los datos del CardView, como nombreMarcaText
        holder.tv.setText(modelo.getNombreMarca());
        holder.tv1.setText(modelo.getNombreModelo());

        // Carga la imagen desde la URL almacenada en Firestore
        String imagenUrl = modelo.getImagenModelo();

        Glide.with(holder.itemView.getContext())
                .load(imagenUrl)
                .fitCenter()
                .into(holder.iv);
        // ... el resto del código, como el OnClickListener

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí implementas la función que deseas ejecutar al hacer clic en el CardView
                // Por ejemplo, puedes mostrar un Toast con un mensaje:

                // Obtener una referencia a la vista a animar (el CardView)
                View animView = v.findViewById(R.id.cvMeGustas);
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
        holder.meGusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Usuarios");
                // Obtener el nombre y la imagen del modelo de coche

                // Verificar si el modelo de coche ya está en la lista de "me gusta" en la base de datos de Firebase
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    DatabaseReference likesRef = database.child(userId).child("meGustas").child(modelo.getNombreModelo());
                    likesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // El modelo ya está en la lista de "me gusta", eliminarlo
                                likesRef.removeValue();
                                Toast.makeText(context, "Ya no te gusta este coche", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Te gusta este coche", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Error al leer los datos
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv, tv1;
        CardView cardView;
        ImageButton meGusta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.iconoCartaMeGustas);
            tv = itemView.findViewById(R.id.textViewCardMeGustas);
            tv1 = itemView.findViewById(R.id.textViewCardMeGustas2);
            meGusta = itemView.findViewById(R.id.imageButton4);
            cardView = itemView.findViewById(R.id.cvMeGustas);

        }
    }
}