package com.example.kisupercars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompararModelos extends AppCompatActivity {

    // Obtén una referencia a la ubicación de las marcas en la base de datos
    DatabaseReference marcasRef = FirebaseDatabase.getInstance().getReference("Marcas");
    Spinner spinner, spinner2 ,spinner3, spinner4;
    TextView tx_anio, tx_motor, tx_nombreMarca, tx_nombreModelo, tx_parMotor, tx_potencia, tx_tiempos, tx_velocidad,
            tx_anio2, tx_motor2, tx_nombreMarca2, tx_nombreModelo2, tx_parMotor2, tx_potencia2, tx_tiempos2, tx_velocidad2;
    ImageButton volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparar_modelos);

        //Quitar el statusbar y tabbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Elementos
        spinner = findViewById(R.id.spinner);
        spinner3 = findViewById(R.id.spinner3);
        spinner2 = findViewById(R.id.spinner2);
        spinner4 = findViewById(R.id.spinner4);
        tx_nombreMarca = findViewById(R.id.textViewMarca1Comparador);
        tx_nombreMarca2 = findViewById(R.id.textViewMarca2Comparador);
        tx_nombreModelo = findViewById(R.id.textViewModelo1Comparador);
        tx_nombreModelo2 = findViewById(R.id.textViewModelo2Comparador);
        tx_potencia = findViewById(R.id.textViewPotencia1Comparador);
        tx_potencia2 = findViewById(R.id.textViewPotencia2Comparador);
        tx_motor = findViewById(R.id.textViewMotor1Comparador);
        tx_motor2 = findViewById(R.id.textViewMotor2Comparador);
        tx_velocidad = findViewById(R.id.textViewVelocidad1Comparador);
        tx_velocidad2 = findViewById(R.id.textViewVelocidad2Comparador);
        tx_tiempos = findViewById(R.id.textViewTiempos1Comparador);
        tx_tiempos2 = findViewById(R.id.textViewTiempos2Comparador);
        tx_anio = findViewById(R.id.textViewAnio1Comparador);
        tx_anio2 = findViewById(R.id.textViewAnio2Comparador);
        tx_parMotor = findViewById(R.id.textViewNM1Comparador);
        tx_parMotor2 = findViewById(R.id.textViewNM2Comparador);
        volver = findViewById(R.id.imageButton);

        //Metodos
        animacionBotones();
        cargarSpinnerMarcas1();
        cargarSpinnerModelos1();
        cargarSpinnerMarcas2();
        cargarSpinnerModelos2();
    }
    @Override
    public void onBackPressed(){//Al pulsar en el boton de retroceder de nuestro telefono
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }

    public void cargarSpinnerMarcas1(){
        // Agrega un ValueEventListener para escuchar los cambios en los datos
        marcasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> marcas = new ArrayList<>();
                marcas.add("- Elige marca -");

                // Recorre los hijos de la ubicación de las marcas y agrega sus nombres a la lista
                for (DataSnapshot marcaSnapshot : snapshot.getChildren()) {
                    Marcas marca = marcaSnapshot.getValue(Marcas.class);
                    assert marca != null;
                    String nombreMarca = marca.getNombreMarca();
                    marcas.add(nombreMarca);
                }

                // Crea un adaptador personalizado para el Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CompararModelos.this, android.R.layout.simple_spinner_item, marcas);
                adapter.setDropDownViewResource(R.layout.estilo_items_spinner);

                // Establece el adaptador del Spinner
                spinner.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Maneja el error de la consulta
            }
        });
    }

    public void cargarSpinnerModelos1(){
// Agrega un OnItemSelectedListener para escuchar los cambios en la selección del primer Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String marcaSeleccionada = parent.getItemAtPosition(position).toString();

                // Obtén una referencia a la ubicación de los modelos de la marca seleccionada en la base de datos
                DatabaseReference modelosRef = FirebaseDatabase.getInstance().getReference("Marcas").child(marcaSeleccionada).child("modelos");

                // Agrega un ValueEventListener para escuchar los cambios en los datos
                modelosRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> modelos = new ArrayList<>();

                        // Recorre los hijos de la ubicación de los modelos y agrega sus nombres a la lista
                        for (DataSnapshot modeloSnapshot : snapshot.getChildren()) {
                            Modelos modelo = modeloSnapshot.getValue(Modelos.class);
                            assert modelo != null;
                            String nombreModelo = modelo.getNombreModelo();
                            modelos.add(nombreModelo);
                        }

                        // Crea un adaptador personalizado para el segundo Spinner
                        ArrayAdapter<String> modelosAdapter = new ArrayAdapter<>(CompararModelos.this,
                                android.R.layout.simple_spinner_item, modelos);
                        modelosAdapter.setDropDownViewResource(R.layout.estilo_items_spinner);

                        // Establece el adaptador del segundo Spinner
                        spinner3.setAdapter(modelosAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Maneja el error de la consulta
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó ningún elemento
            }
        });


    }

    public void cargarSpinnerMarcas2(){
        // Agrega un ValueEventListener para escuchar los cambios en los datos
        marcasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> marcas = new ArrayList<>();
                marcas.add("- Elige marca -");

                // Recorre los hijos de la ubicación de las marcas y agrega sus nombres a la lista
                for (DataSnapshot marcaSnapshot : snapshot.getChildren()) {
                    Marcas marca = marcaSnapshot.getValue(Marcas.class);
                    assert marca != null;
                    String nombreMarca = marca.getNombreMarca();
                    marcas.add(nombreMarca);
                }

                // Crea un adaptador personalizado para el Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CompararModelos.this, android.R.layout.simple_spinner_item, marcas);
                adapter.setDropDownViewResource(R.layout.estilo_items_spinner);

                // Establece el adaptador del Spinner
                spinner2.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Maneja el error de la consulta
            }
        });
    }

    public void cargarSpinnerModelos2(){
// Agrega un OnItemSelectedListener para escuchar los cambios en la selección del primer Spinner
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String marcaSeleccionada = parent.getItemAtPosition(position).toString();

                // Obtén una referencia a la ubicación de los modelos de la marca seleccionada en la base de datos
                DatabaseReference modelosRef = FirebaseDatabase.getInstance().getReference("Marcas").child(marcaSeleccionada).child("modelos");

                // Agrega un ValueEventListener para escuchar los cambios en los datos
                modelosRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> modelos = new ArrayList<>();

                        // Recorre los hijos de la ubicación de los modelos y agrega sus nombres a la lista
                        for (DataSnapshot modeloSnapshot : snapshot.getChildren()) {
                            Modelos modelo = modeloSnapshot.getValue(Modelos.class);
                            assert modelo != null;
                            String nombreModelo = modelo.getNombreModelo();
                            modelos.add(nombreModelo);
                        }

                        // Crea un adaptador personalizado para el segundo Spinner
                        ArrayAdapter<String> modelosAdapter = new ArrayAdapter<>(CompararModelos.this,
                                android.R.layout.simple_spinner_item, modelos);
                        modelosAdapter.setDropDownViewResource(R.layout.estilo_items_spinner);

                        // Establece el adaptador del segundo Spinner
                        spinner4.setAdapter(modelosAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Maneja el error de la consulta
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó ningún elemento
            }
        });


    }

    public void botonSeleccionar1(View view){
        try {
            String marcaSeleccionada = spinner.getSelectedItem().toString();
            String modeloSeleccionado = spinner3.getSelectedItem().toString();

                // Obtén una referencia a la ubicación de las características del modelo seleccionado en la base de datos
                DatabaseReference caracteristicasRef = FirebaseDatabase.getInstance().getReference("Marcas")
                        .child(marcaSeleccionada)
                        .child("modelos")
                        .child(modeloSeleccionado)
                        .child("caracteristicas");

                // Agrega un ValueEventListener para escuchar los cambios en los datos
                caracteristicasRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Caracteristicas caracteristicas = snapshot.getValue(Caracteristicas.class);
                        // Recupera las características del modelo seleccionado
                        String anio = caracteristicas.getAnio();
                        String motor = caracteristicas.getMotor();
                        String nombreMarca = caracteristicas.getNombreMarca();
                        String nombreModelo = caracteristicas.getNombreModelo();
                        String parMotor = caracteristicas.getParMotor();
                        String potencia = caracteristicas.getPotencia();
                        String tiempo = caracteristicas.getTiempo();
                        String velocidad = caracteristicas.getVelocidad();
                        // Agrega más características según sea necesario

                        // Actualiza los TextViews con los valores recuperados
                        tx_anio.setText(anio);
                        tx_motor.setText(motor);
                        tx_nombreMarca.setText(nombreMarca);
                        tx_nombreModelo.setText(nombreModelo);
                        tx_parMotor.setText(parMotor);
                        tx_potencia.setText(potencia);
                        tx_tiempos.setText(tiempo);
                        tx_velocidad.setText(velocidad);
                        // Actualiza más TextViews según las características que desees mostrar
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Maneja el error de la consulta
                    }
                });

        }catch (Exception e){
            Toast.makeText(this, "Selecciona un modelo por favor", Toast.LENGTH_SHORT).show();
        }
    }

    public void botonSeleccionar2(View view){
        try {

            String marcaSeleccionada = spinner2.getSelectedItem().toString();
            String modeloSeleccionado = spinner4.getSelectedItem().toString();

            // Obtén una referencia a la ubicación de las características del modelo seleccionado en la base de datos
            DatabaseReference caracteristicasRef = FirebaseDatabase.getInstance().getReference("Marcas")
                    .child(marcaSeleccionada)
                    .child("modelos")
                    .child(modeloSeleccionado)
                    .child("caracteristicas");

            // Agrega un ValueEventListener para escuchar los cambios en los datos
            caracteristicasRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Caracteristicas caracteristicas = snapshot.getValue(Caracteristicas.class);
                    // Recupera las características del modelo seleccionado
                    String anio = caracteristicas.getAnio();
                    String motor = caracteristicas.getMotor();
                    String nombreMarca = caracteristicas.getNombreMarca();
                    String nombreModelo = caracteristicas.getNombreModelo();
                    String parMotor = caracteristicas.getParMotor();
                    String potencia = caracteristicas.getPotencia();
                    String tiempo = caracteristicas.getTiempo();
                    String velocidad = caracteristicas.getVelocidad();
                    // Agrega más características según sea necesario

                    // Actualiza los TextViews con los valores recuperados
                    tx_anio2.setText(anio);
                    tx_motor2.setText(motor);
                    tx_nombreMarca2.setText(nombreMarca);
                    tx_nombreModelo2.setText(nombreModelo);
                    tx_parMotor2.setText(parMotor);
                    tx_potencia2.setText(potencia);
                    tx_tiempos2.setText(tiempo);
                    tx_velocidad2.setText(velocidad);
                    // Actualiza más TextViews según las características que desees mostrar
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Maneja el error de la consulta
                }

            });
        }catch (Exception e){
            Toast.makeText(this, "Selecciona un modelo por favor", Toast.LENGTH_SHORT).show();
        }
    }

    public void volver(View view){
        Intent intent = new Intent(CompararModelos.this, Home.class);
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
                }
                // Retornamos false para que el evento onTouch sea procesado por el ImageButton
                return false;
            }
        });
    }
}