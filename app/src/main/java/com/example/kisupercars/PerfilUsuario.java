package com.example.kisupercars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilUsuario extends AppCompatActivity {



    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private EditText nombre, email, numero;
    ImageButton volver, editar, borrar;
    Button cancelar, confirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        //Quitar tabbar y statusbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Usuarios");

        //Elementos
        nombre = findViewById(R.id.editTextTextNombrePerfil);
        email = findViewById(R.id.editTextTextEmailAddress);
        numero = findViewById(R.id.editTextPhone);
        volver = findViewById(R.id.imageButtonVolver);
        editar = findViewById(R.id.imageButton3);
        borrar = findViewById(R.id.imageButton2);
        cancelar = findViewById(R.id.button3);
        confirmar = findViewById(R.id.button4);

        //Metodos iniciales
        InitElements();
        animacionBotones();
        buscarUsuario();

    }//Fin del onCreate

    public void InitElements(){
        cancelar.setVisibility(View.INVISIBLE);
        confirmar.setVisibility(View.INVISIBLE);
        nombre.setEnabled(false);
        email.setEnabled(false);
        numero.setEnabled(false);
    }

    public void buscarUsuario(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            getUserData(uid);
        } else {
            Toast.makeText(PerfilUsuario.this, "No hay usuario autenticado.", Toast.LENGTH_SHORT).show();
        }
    }

    //Te muestra los datos de usuario
    private void getUserData(String uid) {
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("nombre").getValue(String.class);
                String userEmail = dataSnapshot.child("email").getValue(String.class);
                String userNumber = dataSnapshot.child("numeroTelefono").getValue(String.class);

                nombre.setText(userName);
                email.setText(userEmail);
                numero.setText(userNumber);

                if(nombre.getText().toString().equals("") && email.getText().toString().equals("") &&
                numero.getText().toString().equals("")){
                    editar.setEnabled(false);
                    borrar.setEnabled(false);
                }else{
                    editar.setEnabled(true);
                    borrar.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PerfilUsuario.this, "Error al obtener datos del usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void volver(View view){
        Intent intent = new Intent(PerfilUsuario.this, Home.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }

    public void botonesVisibles(View view){
        cancelar.setVisibility(View.VISIBLE);
        confirmar.setVisibility(View.VISIBLE);
        nombre.setEnabled(true);
        nombre.setBackgroundResource(R.drawable.estilo_cajon_texto);
        email.setEnabled(false);
        numero.setEnabled(true);
        numero.setBackgroundResource(R.drawable.estilo_cajon_texto);
    }

    public void botonesInvisibles(View view){
        cancelar.setVisibility(View.INVISIBLE);
        confirmar.setVisibility(View.INVISIBLE);
        nombre.setEnabled(false);
        nombre.setBackgroundResource(R.drawable.estilo_cajon_texto_perfilusuario);
        email.setEnabled(false);
        numero.setEnabled(false);
        numero.setBackgroundResource(R.drawable.estilo_cajon_texto_perfilusuario);
    }

    public void confirmarCambios(View v){
        // Inflar el diseño personalizado
        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.estilo_alert_dialog_confirmar, null);
        // Crear el objeto de AlertDialog.Builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PerfilUsuario.this);

        // Establecer el contenido personalizado del AlertDialog
        alertDialogBuilder.setView(customView);
        // Crear el AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Establecer el fondo del contenedor de la ventana del diálogo como transparente
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Configurar los botones y sus OnClickListener
        Button positiveButton = customView.findViewById(R.id.dialog_boton_positivo);
        Button negativeButton = customView.findViewById(R.id.dialog_boton_negativo);

        positiveButton.setOnClickListener(view -> {
            // Cerrar sesión y navegar a MainActivity
            alertDialog.dismiss();
        });

        negativeButton.setOnClickListener(view -> {
            // Si el usuario hace clic en "No", se cierra el AlertDialog
            alertDialog.dismiss();
        });

        // Mostrar el AlertDialog
        alertDialog.show();
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
        // Agregamos un listener para el evento onTouch del ImageButton
        borrar.setOnTouchListener(new View.OnTouchListener() {
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
                    borrar.startAnimation(scaleAnimation);



                }



                // Detectamos si se ha soltado el botón
                else if (event.getAction() == MotionEvent.ACTION_UP) {



                    // Creamos una animación de escala para volver a su tamaño original
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(100);
                    scaleAnimation.setFillAfter(true);



                    // Aplicamos la animación al botón
                    borrar.startAnimation(scaleAnimation);
                }
                // Retornamos false para que el evento onTouch sea procesado por el ImageButton
                return false;
            }
        });
        // Agregamos un listener para el evento onTouch del ImageButton
        editar.setOnTouchListener(new View.OnTouchListener() {
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
                    editar.startAnimation(scaleAnimation);



                }



                // Detectamos si se ha soltado el botón
                else if (event.getAction() == MotionEvent.ACTION_UP) {



                    // Creamos una animación de escala para volver a su tamaño original
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(100);
                    scaleAnimation.setFillAfter(true);



                    // Aplicamos la animación al botón
                    editar.startAnimation(scaleAnimation);
                }
                // Retornamos false para que el evento onTouch sea procesado por el ImageButton
                return false;
            }
        });
    }





    private void ocultarTeclado(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    } // Cierra el método ocultarTeclado.
}