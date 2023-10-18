package com.example.kisupercars;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextContrasenia;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageButton ib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Quitar statusbar y tabbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Inicializar Firebase
        FirebaseApp.initializeApp(this);

        //Elementos de la interfaz
        editTextNombre = findViewById(R.id.loginUsuario);
        editTextContrasenia = findViewById(R.id.loginContraseña);
        ib = findViewById(R.id.botonGoogle);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        usuarioConectado();
    }//Fin del onCreate

    public void usuarioConectado(){//Busco si hay algun usuario logueado activamente
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(MainActivity.this, Home.class));
            finish();
        } else {
            Toast.makeText(MainActivity.this, "No hay usuario autenticado.", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());
                    Toast.makeText(this, "Conexion correcta", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Home.class));
                    //datosUsuariosGoogle(account);
                    finish();
                } catch (ApiException e) {
                    Log.w(TAG, "Google sign in failed", e);
                    Toast.makeText(this, "Fallo de conexion", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Manejar el caso cuando el cuadro de diálogo de inicio de sesión se cierra o se cancela
                Toast.makeText(this, "Inicio de sesión cancelado", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Handle successful sign in
                        } else {
                            // Handle failed sign in
                        }
                    }
                });
    }

    public void navegarRegistroUsuarios(View view){
        Intent intent = new Intent(this, RegistroUsuarios.class);
        startActivity(intent);
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso, navega a la siguiente Activity
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(MainActivity.this, Home.class));
                        finish();
                    } else {
                        // Si el inicio de sesión falla, muestra un mensaje al usuario
                        ocultarTeclado();
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void iniciar(View view){
        String nombre = editTextNombre.getText().toString().trim();
        String contrasenia = editTextContrasenia.getText().toString().trim();

        if(validarInicioSesion()){
            loginUser(nombre,contrasenia);
        }else{

        }
    }

    public boolean validarInicioSesion(){
        if (editTextNombre.getText().toString().isEmpty()) {
            ocultarTeclado();
            editTextNombre.setError("Ingrese su nombre");
            return false;
        } else {
            editTextNombre.setError(null);
        }

        if (editTextContrasenia.getText().toString().isEmpty()) {
            ocultarTeclado();
            editTextContrasenia.setError("Ingrese su contraseña");
            return false;
        } else {
            editTextContrasenia.setError(null);
        }
        return true;
    }

    private void ocultarTeclado(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    } // Cierra el método ocultarTeclado.



}