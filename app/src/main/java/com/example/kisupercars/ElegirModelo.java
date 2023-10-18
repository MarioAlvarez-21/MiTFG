package com.example.kisupercars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ElegirModelo extends AppCompatActivity implements SearchView.OnQueryTextListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference marcasRef = database.getReference("Marcas");
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    List<Modelos> listaModelos = new ArrayList<>();
    RecyclerView modelosRecyclerView;
    SearchView searchView;
    ImageButton volver;
    ImageView iv_marca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_modelo);

        //Quitar statusbar y tabbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Marca recibida del intent
        String marcaRecibida = getIntent().getStringExtra("marca");

        //Elementos
        iv_marca = findViewById(R.id.imageViewMarcaElegirModelos);
        volver = findViewById(R.id.imageButtonModelosVolver);
        modelosRecyclerView = findViewById(R.id.rv_elegirmodelos);
        searchView = findViewById(R.id.sv_modelos);
        searchView.setOnQueryTextListener(this);

        //Metodos
        animacionBotones();
        recibirModelos(marcaRecibida);
    }

    //Carga el RecyclerView con los modelos
    public void recibirModelos(String marca){
        marcasRef.child(marca).child("modelos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot modeloSnapshot : dataSnapshot.getChildren()) {
                    Modelos modelo = modeloSnapshot.getValue(Modelos.class);
                    // Obtener la URL de descarga de la imagen desde Firebase Storage
                    storageRef.child("LogosModelos/" + modelo.getNombreModelo() + ".png")
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    modelo.setImagenModelo(uri.toString());
                                    listaModelos.add(modelo);

                                    if (listaModelos.size() == dataSnapshot.getChildrenCount()) {
                                        // Inicializa el RecyclerView con la lista de marcas
                                        initRecyclerView();
                                    }
                                }
                            });
                    // Aquí puedes cargar la imagen del modelo usando Picasso o Glide
                    StorageReference storageRef2 = FirebaseStorage.getInstance().getReference("LogosMarcas/" + modelo.getNombreMarca() + ".png");

                    storageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();

                            Glide.with(ElegirModelo.this)
                                    .load(imageUrl)
                                    .into(iv_marca);
                        }
                    });
                }
                // Configurar el adaptador del RecyclerView aquí
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error aquí
            }
        });


    }

    // Inicializa el RecyclerView con la lista de modelos
    public void initRecyclerView(){

        ModelosAdapter adapter = new ModelosAdapter(ElegirModelo.this, listaModelos);
        modelosRecyclerView.setAdapter(adapter);
        modelosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    //Metodos del SearchView
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<Modelos> filteredModelos = new ArrayList<>();

        for (Modelos modelo : listaModelos) {
            if (modelo.getNombreModelo().toLowerCase().contains(userInput)) {
                filteredModelos.add(modelo);
            }
        }

        updateRecyclerView(filteredModelos);
        return true;
    }
    //Fin de los metodos del SearchView

    //Metodo para actualizar el RecyclerView con la lista de modelos
    private void updateRecyclerView(ArrayList<Modelos> filteredModelos) {
        ModelosAdapter adapter = new ModelosAdapter(this, filteredModelos);
        RecyclerView recyclerView = findViewById(R.id.rv_elegirmodelos);
        recyclerView.setAdapter(adapter);
    }

    public void volver(View view){
        finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void animacionBotones(){
        // Agregamos un listener para el evento onTouch del ImageButton
        volver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Detectamos si se ha pulsado el botón
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    // Creamos una animación de escala para hacer el botón más pequeño
                    ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(100);
                    scaleAnimation.setFillAfter(true);

                    // Aplicamos la animación al botón
                    volver.startAnimation(scaleAnimation);

                }

                // Detectamos si se ha soltado el botón
                else if (event.getAction() == MotionEvent.ACTION_UP) {

                    // Creamos una animación de escala para volver a su tamaño original
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(100);
                    scaleAnimation.setFillAfter(true);

                    // Aplicamos la animación al botón
                    volver.startAnimation(scaleAnimation);
                }
                // Retornamos false para que el evento onTouch sea procesado por el ImageButton
                return false;
            }
        });
    }

}