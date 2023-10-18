package com.example.kisupercars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class CaracteristicasModelo extends AppCompatActivity {

    TextView anioTextView, descripcionTextView, motorTextView, parMotorTextView, potenciaTextView, tiempoTextView,
    velocidadTextView, modeloTextView, marcaTextView;
    ImageView modeloImageView;
    ImageButton meGustas, volver;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caracteristicas_modelo);

        //Quitar statusbar y tabbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Datos recibidos del intent
        String marcaRecibida = getIntent().getStringExtra("marca");
        String modeloRecibido = getIntent().getStringExtra("modelo");

        //Elementos
        anioTextView = findViewById(R.id.textView11);
        descripcionTextView = findViewById(R.id.textView13);
        motorTextView = findViewById(R.id.textView2);
        parMotorTextView = findViewById(R.id.textView12);
        potenciaTextView = findViewById(R.id.textView);
        tiempoTextView = findViewById(R.id.textView10);
        velocidadTextView = findViewById(R.id.textView3);
        modeloTextView = findViewById(R.id.tx_nombreModeloCaracteristicas);
        marcaTextView = findViewById(R.id.tx_nombreMarcaCaracteristicas);
        modeloImageView = findViewById(R.id.imageViewModeloElegido);
        meGustas = findViewById(R.id.imageButtonCaracteristicasMeGusta);
        volver = findViewById(R.id.imageButtonVolverCaracteristicas);

        //Metodos
        recibirmodelo(marcaRecibida, modeloRecibido);
        comprobarMeGusta(modeloRecibido);
        meGusta(marcaRecibida, modeloRecibido);
        animacionBotones();

    }//Fin del metodo onCreate

    public void comprobarMeGusta(String modelo){
        // Verificar si el modelo de coche ya está en la lista de "me gusta" en la base de datos de Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference likesRef = database.child(userId).child("meGustas").child(modelo);
            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // El modelo ya está en la lista de "me gusta"
                        meGustas.setImageResource(R.drawable.favorito);
                    } else {
                        // El modelo no está en la lista de "me gusta"
                        meGustas.setImageResource(R.drawable.favoritosinfondo);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Error al leer los datos
                }
            });
        }
    }

    //Añadir o quitar un coche de la seccion 'me gustas'
    public void meGusta(String marca, String modelo){
            meGustas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener el nombre y la imagen del modelo de coche
                    String imagenModelo = "";

                    // Verificar si el modelo de coche ya está en la lista de "me gusta" en la base de datos de Firebase
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        DatabaseReference likesRef = database.child(userId).child("meGustas").child(modelo);
                        likesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // El modelo ya está en la lista de "me gusta", eliminarlo
                                    likesRef.removeValue();
                                    meGustas.setImageResource(R.drawable.favoritosinfondo);
                                    Toast.makeText(CaracteristicasModelo.this, "Ya no te gusta este coche", Toast.LENGTH_SHORT).show();
                                } else {
                                    // El modelo no está en la lista de "me gusta", agregarlo
                                    likesRef.child("nombreModelo").setValue(modelo);
                                    likesRef.child("imagenModelo").setValue(imagenModelo);
                                    likesRef.child("nombreMarca").setValue(marca);
                                    meGustas.setImageResource(R.drawable.favorito);
                                    Toast.makeText(CaracteristicasModelo.this, "Te gusta este coche", Toast.LENGTH_SHORT).show();
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

    //Carga los datos del modelo seleccionado
    public void recibirmodelo(String marca, String modelo){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Marcas").child(marca).child("modelos").child(modelo).child("caracteristicas");
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Caracteristicas caracteristicas = snapshot.getValue(Caracteristicas.class);
                if (caracteristicas != null) {
                    String modelo = caracteristicas.getNombreModelo();
                    String anio = caracteristicas.getAnio();
                    String descripcion = caracteristicas.getDescripcion();
                    String motor = caracteristicas.getMotor();
                    String marca = caracteristicas.getNombreMarca();
                    String imagenModelo = caracteristicas.getImagenModelo();
                    String parMotor = caracteristicas.getParMotor();
                    String potencia = caracteristicas.getPotencia();
                    String tiempo = caracteristicas.getTiempo();
                    String velocidad = caracteristicas.getVelocidad();

                    // Configura los elementos de la interfaz de usuario con los datos
                    anioTextView.setText(anio);
                    descripcionTextView.setText(descripcion);
                    motorTextView.setText(motor);
                    parMotorTextView.setText(parMotor);
                    potenciaTextView.setText(potencia);
                    tiempoTextView.setText(tiempo);
                    velocidadTextView.setText(velocidad);
                    modeloTextView.setText(modelo);
                    marcaTextView.setText(marca);
                    // Aquí puedes cargar la imagen del modelo usando Picasso o Glide
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference("LogosModelos/" + caracteristicas.getNombreModelo() + ".png");

                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();

                            Glide.with(CaracteristicasModelo.this)
                                    .load(imageUrl)
                                    .into(modeloImageView);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Maneja el error de la consulta
            }
        });
    }

    public void volver(View view){
        finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void animacionBotones(){
        // Agregamos un listener para el evento onTouch del ImageButton
        meGustas.setOnTouchListener(new View.OnTouchListener() {
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
                    meGustas.startAnimation(scaleAnimation);

                }

                // Detectamos si se ha soltado el botón
                else if (event.getAction() == MotionEvent.ACTION_UP) {

                    // Creamos una animación de escala para volver a su tamaño original
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(100);
                    scaleAnimation.setFillAfter(true);

                    // Aplicamos la animación al botón
                    meGustas.startAnimation(scaleAnimation);
                }
                // Retornamos false para que el evento onTouch sea procesado por el ImageButton
                return false;
            }
        });
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
}//Fin de la clase