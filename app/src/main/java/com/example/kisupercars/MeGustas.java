package com.example.kisupercars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MeGustas extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ImageButton volver;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Usuarios");
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    List<Modelos> listaMeGustas = new ArrayList<>();
    RecyclerView meGustasRecyclerView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_gustas);

        //Quitar statusbar y tabbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //Elementos de la interfaz
        volver = findViewById(R.id.imageButtonVolverMeGustas);
        meGustasRecyclerView = findViewById(R.id.rv_modelosMeGusta);
        searchView = findViewById(R.id.sv_modelosMeGusta);
        searchView.setOnQueryTextListener(this);

        //Metodos
        recibirModelos();
        animacionBotones();

    }//Fin onCreate

    //Carga los modelos que le gustan al usuario iniciado
    public void recibirModelos(){
        // Verificar si el modelo de coche ya está en la lista de "me gusta" en la base de datos de Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // El número de columnas en la cuadrícula
        int numberOfColumns = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        meGustasRecyclerView.setLayoutManager(layoutManager);

        if (currentUser != null) {

            String userId = currentUser.getUid();
            DatabaseReference likesRef = database.child(userId).child("meGustas");

            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot modeloSnapshot : dataSnapshot.getChildren()) {
                        listaMeGustas.clear();
                        Modelos modelo = modeloSnapshot.getValue(Modelos.class);

                        // Obtener la URL de descarga de la imagen desde Firebase Storage
                        storageRef.child("LogosModelos/" + modelo.getNombreModelo() + ".png")
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        modelo.setImagenModelo(uri.toString());
                                        listaMeGustas.add(modelo);

                                        if (listaMeGustas.size() == dataSnapshot.getChildrenCount()) {
                                            // Inicializa el RecyclerView con la lista de marcas
                                            initRecyclerView();
                                        }
                                    }
                                });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Error al leer los datos
                }
            });
        }


    }

    //Iniciar recyclerview
    @SuppressLint("NotifyDataSetChanged")
    public void initRecyclerView(){
        MeGustasAdapter adapter;

        // El número de columnas en la cuadrícula
        int numberOfColumns = 2;

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        meGustasRecyclerView.setLayoutManager(layoutManager);

        adapter = new MeGustasAdapter(MeGustas.this, listaMeGustas);
        adapter.notifyDataSetChanged();
        meGustasRecyclerView.setAdapter(adapter);


    }


    //Metodos obligatorios searchview
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<Modelos> filteredModelos = new ArrayList<>();

        for (Modelos modelo : listaMeGustas) {
            if (modelo.getNombreModelo().toLowerCase().contains(userInput) && modelo.getNombreMarca().toLowerCase().contains(userInput)) {
                filteredModelos.add(modelo);
            }else if(modelo.getNombreModelo().toLowerCase().contains(userInput)){
                filteredModelos.add(modelo);
            }else if(modelo.getNombreMarca().toLowerCase().contains(userInput)){
                filteredModelos.add(modelo);
            }
        }

        updateRecyclerView(filteredModelos);
        return true;
    }
    //Fin de metodos searchview

    //Se actualiza el recyclerview mientras escribes en el searchview
    private void updateRecyclerView(ArrayList<Modelos> filteredModelos) {
        MeGustasAdapter adapter = new MeGustasAdapter(this, filteredModelos);
        RecyclerView recyclerView = findViewById(R.id.rv_modelosMeGusta);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed(){//Cuando das al boton de retroceder de tu movil
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
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

                    //Voy a la activity favoritos
                    Intent intent = new Intent(MeGustas.this, Home.class);
                    startActivity(intent);
                    finish();
                }

                // Retornamos false para que el evento onTouch sea procesado por el ImageButton
                return false;
            }
        });
    }
}