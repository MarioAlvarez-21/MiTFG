package com.example.kisupercars;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Marcas");
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    List<Marcas> listaMarcas = new ArrayList<>();
    RecyclerView marcasRecyclerView;
    ImageButton ibPerfilUsuario, ibSalir, ibMeGusta, ibComparar;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Quitar status bar y tabbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Elementos de la interfaz
        ibPerfilUsuario = findViewById(R.id.imageButtonPerfilUsuario);
        ibSalir = findViewById(R.id.imageButtonSalir);
        ibComparar = findViewById(R.id.imageButtonComparar);
        ibMeGusta = findViewById(R.id.imageButtonMeGusta);
        marcasRecyclerView = findViewById(R.id.rv);
        mAuth = FirebaseAuth.getInstance();

        // Configura las opciones de inicio de sesión de Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Inicializa mGoogleSignInClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Metodos
        cerrarSesion();
        listarMarcas();
        efectoBotones();

    }//Fin onCreate


    //Carga las marcas en el recyclerview
    public void listarMarcas(){
        // El número de columnas en la cuadrícula
        int numberOfColumns = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        marcasRecyclerView.setLayoutManager(layoutManager);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot marcaSnapshot : dataSnapshot.getChildren()) {
                    Marcas marca = marcaSnapshot.getValue(Marcas.class);

                    // Obtener la URL de descarga de la imagen desde Firebase Storage
                    storageRef.child("LogosMarcas/" + marca.getNombreMarca() + ".png")
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    marca.setImagenUrl(uri.toString());
                                    listaMarcas.add(marca);

                                    if (listaMarcas.size() == dataSnapshot.getChildrenCount()) {
                                        // Inicializa el RecyclerView con la lista de marcas
                                        initRecyclerView();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Maneja los errores aquí

            }
        });
    }

    //Inicializa el RecyclerView con la lista de marcas
    public void initRecyclerView(){
        // El número de columnas en la cuadrícula
        int numberOfColumns = 3;

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        marcasRecyclerView.setLayoutManager(layoutManager);

        MarcasAdapter adapter = new MarcasAdapter(Home.this, listaMarcas);
        marcasRecyclerView.setAdapter(adapter);

    }
    public void cerrarSesion(){
            ibSalir.setOnClickListener(v -> {
                // Inflar el diseño personalizado
                LayoutInflater inflater = getLayoutInflater();
                View customView = inflater.inflate(R.layout.estilo_alert_dialog, null);
                // Crear el objeto de AlertDialog.Builder
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);

                // Establecer el contenido personalizado del AlertDialog
                alertDialogBuilder.setView(customView);
                // Crear el AlertDialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // Establecer el fondo del contenedor de la ventana del diálogo como transparente
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Configurar los botones y sus OnClickListener
                Button positiveButton = customView.findViewById(R.id.dialog_positive_button);
                Button negativeButton = customView.findViewById(R.id.dialog_negative_button);

                positiveButton.setOnClickListener(view -> {
                    // Cerrar sesión y navegar a MainActivity
                    mAuth.signOut();
                    mGoogleSignInClient.signOut();
                    startActivity(new Intent(Home.this, MainActivity.class));
                    finish();
                    alertDialog.dismiss();
                });

                negativeButton.setOnClickListener(view -> {
                    // Si el usuario hace clic en "No", se cierra el AlertDialog
                    alertDialog.dismiss();
                });

                // Mostrar el AlertDialog
                alertDialog.show();
            });
    }


    @SuppressLint("ClickableViewAccessibility")
    public void efectoBotones(){
        // Agregamos un listener para el evento onTouch del ImageButton
        ibPerfilUsuario.setOnTouchListener(new View.OnTouchListener() {
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
                    ibPerfilUsuario.startAnimation(scaleAnimation);

                }

                // Detectamos si se ha soltado el botón
                else if (event.getAction() == MotionEvent.ACTION_UP) {

                    // Creamos una animación de escala para volver a su tamaño original
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(100);
                    scaleAnimation.setFillAfter(true);

                    // Aplicamos la animación al botón
                    ibPerfilUsuario.startAnimation(scaleAnimation);

                    //Voy a la activity favoritos
                    Intent intent = new Intent(Home.this, PerfilUsuario.class);
                    startActivity(intent);
                    finish();


                }

                // Retornamos false para que el evento onTouch sea procesado por el ImageButton
                return false;
            }
        });

        // Agregamos un listener para el evento onTouch del ImageButton
        ibSalir.setOnTouchListener(new View.OnTouchListener() {
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
                    ibSalir.startAnimation(scaleAnimation);

                }

                // Detectamos si se ha soltado el botón
                else if (event.getAction() == MotionEvent.ACTION_UP) {

                    // Creamos una animación de escala para volver a su tamaño original
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(100);
                    scaleAnimation.setFillAfter(true);

                    // Aplicamos la animación al botón
                    ibSalir.startAnimation(scaleAnimation);
                }

                // Retornamos false para que el evento onTouch sea procesado por el ImageButton
                return false;
            }
        });
        // Agregamos un listener para el evento onTouch del ImageButton
        ibMeGusta.setOnTouchListener(new View.OnTouchListener() {
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
                    ibMeGusta.startAnimation(scaleAnimation);

                }

                // Detectamos si se ha soltado el botón
                else if (event.getAction() == MotionEvent.ACTION_UP) {

                    // Creamos una animación de escala para volver a su tamaño original
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(100);
                    scaleAnimation.setFillAfter(true);

                    // Aplicamos la animación al botón
                    ibMeGusta.startAnimation(scaleAnimation);

                    //Voy a la activity favoritos
                    Intent intent = new Intent(Home.this, MeGustas.class);
                    startActivity(intent);
                    finish();


                }

                // Retornamos false para que el evento onTouch sea procesado por el ImageButton
                return false;
            }
        });
        // Agregamos un listener para el evento onTouch del ImageButton
        ibComparar.setOnTouchListener(new View.OnTouchListener() {
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
                    ibComparar.startAnimation(scaleAnimation);

                }

                // Detectamos si se ha soltado el botón
                else if (event.getAction() == MotionEvent.ACTION_UP) {

                    // Creamos una animación de escala para volver a su tamaño original
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(100);
                    scaleAnimation.setFillAfter(true);

                    // Aplicamos la animación al botón
                    ibComparar.startAnimation(scaleAnimation);

                    //Voy a la activity favoritos
                    Intent intent = new Intent(Home.this, CompararModelos.class);
                    startActivity(intent);
                    finish();


                }

                // Retornamos false para que el evento onTouch sea procesado por el ImageButton
                return false;
            }
        });
    }




}