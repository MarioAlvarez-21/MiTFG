package com.example.kisupercars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistroUsuarios extends AppCompatActivity {

    private EditText editTextUsuario,editTextEmail,editTextContrasenia,editTextContrasenia2,editTextTelefono;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);

        //Poner transparentes el tabbar y el statusbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Elementos
        editTextUsuario = findViewById(R.id.registroUsuario);
        editTextEmail = findViewById(R.id.registroEmail);
        editTextContrasenia = findViewById(R.id.registroContraseña);
        editTextContrasenia2 = findViewById(R.id.registroContraseña2);
        editTextTelefono = findViewById(R.id.registroNumero);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        // Agrego listeners TextWatcher
        editTextUsuario.addTextChangedListener(nombreTextWatcher);
        editTextEmail.addTextChangedListener(emailTextWatcher);
        editTextContrasenia.addTextChangedListener(contraseniaTextWatcher);
        editTextContrasenia2.addTextChangedListener(contrasenia2TextWatcher);

    }//Fin del onCreate

    private void createUser(String email, String contrasenia, String nombre, String numeroTelefono) {
        mAuth.createUserWithEmailAndPassword(email, contrasenia)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // El registro fue exitoso, puedes realizar acciones con el usuario registrado
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Por ejemplo, mostrar un mensaje de éxito
                        Toast.makeText(RegistroUsuarios.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();
                        // Llama al método para guardar la información del usuario en la base de datos de Firebase
                        saveUser(nombre, email, numeroTelefono);
                        Intent intent = new Intent(RegistroUsuarios.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // Si el registro falla, muestra un mensaje al usuario
                        if (task.getException() instanceof FirebaseAuthException) {
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            String errorMessage = "Error en el registro.";
                            switch (errorCode) {
                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    errorMessage = "El correo electrónico ya está en uso.";
                                    break;
                                // Puedes agregar más casos de error aquí, si es necesario
                                default:
                                    errorMessage = "Error desconocido en el registro.";
                                    break;
                            }
                            Toast.makeText(RegistroUsuarios.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegistroUsuarios.this, "Error en el registro.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }//Final del metodo crear usuarios

    private void saveUser(String nombre, String email, String numeroTelefono) {
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference ref = mDatabase.getReference("Usuarios").child(user.getUid());

        Map<String, Object> userValues = new HashMap<>();
        userValues.put("nombre", nombre);
        userValues.put("email", email);
        userValues.put("numeroTelefono", numeroTelefono);

        ref.setValue(userValues).addOnSuccessListener(aVoid -> {
                    // El registro fue exitoso, puedes realizar acciones adicionales
                })
                .addOnFailureListener(e -> {
                    // Si el registro falla, muestra un mensaje al usuario
                    Toast.makeText(RegistroUsuarios.this, "Error al guardar información del usuario.", Toast.LENGTH_SHORT).show();
                });
        ;
    }//Fin del metodo guardar usuario



    private TextWatcher nombreTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Validar nombre
            if (s.toString().trim().isEmpty()) {
                editTextUsuario.setError("Ingrese un nombre de usuario");

            }else if(s.toString().contains(" ")){
                editTextUsuario.setError("El usuario no puede tener espacios en blanco");
            }else {
                editTextUsuario.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Validar email
            if (s.toString().trim().isEmpty()) {
                editTextEmail.setError("Ingrese un correo electrónico");
            } else {
                editTextEmail.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private TextWatcher contraseniaTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Validar contraseña
            if (s.toString().trim().isEmpty()) {
                editTextContrasenia.setError("Ingrese una contraseña");
            }else if(s.toString().contains(" ")){
                editTextContrasenia.setError("La contraseña no puede contener espacios");
            }else {
                editTextContrasenia.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private TextWatcher contrasenia2TextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Validar contraseña2
            if (s.toString().trim().isEmpty()) {
                editTextContrasenia2.setError("Ingrese una contraseña");
            }else if(s.toString().contains(" ")){
                editTextContrasenia2.setError("La contraseña no puede contener espacios");
            }else {
                editTextContrasenia2.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    public void cancelarRegistro(View view){
        finish();
    }

    public void registrarUsuarios(View view){
        validarCampos();
        String nombreValor = editTextUsuario.getText().toString().trim();
        String emailValor = editTextEmail.getText().toString().trim();
        String contraseniaValor = editTextContrasenia.getText().toString().trim();
        String telefonoValor = editTextTelefono.getText().toString().trim();
        if(validarCampos()){
            createUser(emailValor, contraseniaValor, nombreValor, telefonoValor);
        }else{

        }

    }



    public boolean validarCampos() {
        // Obtener los valores de los campos de texto
        String nombreValor = editTextUsuario.getText().toString().trim();
        String emailValor = editTextEmail.getText().toString().trim();
        String contraseniaValor = editTextContrasenia.getText().toString().trim();
        String repiteContraseñaValor = editTextContrasenia2.getText().toString().trim();
        String telefonoValor = editTextTelefono.getText().toString().trim();
        //comprueba usuario
        if (nombreValor.isEmpty()) {
            editTextUsuario.setError("Ingrese un nombre de usuario");
            return false;

        }else if(nombreValor.contains(" ")){
            editTextUsuario.setError("El usuario no puede tener espacios en blanco");
            return false;
        }else {
            editTextUsuario.setError(null);
        }
        //comprueba email
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (emailValor.isEmpty()) {
            editTextEmail.setError("Ingrese un correo electrónico");
            return false;
        }else if (!emailValor.matches(emailPattern)) {
            editTextEmail.setError("El correo electrónico ingresado no es válido");
            return false;
        }else {
            editTextEmail.setError(null);
        }

        // Validar contraseña
        if (contraseniaValor.isEmpty()) {
            editTextContrasenia.setError("Ingrese una contraseña");
            return false;
        }else if(contraseniaValor.contains(" ")){
            editTextContrasenia.setError("La contraseña no puede contener espacios");
            return false;
        }else if(contraseniaValor.length() < 6){
            editTextContrasenia.setError("La contraseña tiene que ser mayor de 5 caracteres");
            return false;
        }else {
            editTextUsuario.setError(null);
        }

        // Validar contraseña2
        if(!repiteContraseñaValor.equals(contraseniaValor)){
            editTextContrasenia2.setError("Las contraseñas no coinciden");
            return false;
        }else {
            editTextContrasenia2.setError(null);
        }

        // Validar telefono
        if(!telefonoValor.isEmpty() && telefonoValor.length() < 9 ||
                telefonoValor.length() > 9){
            editTextTelefono.setError("El numero tiene que tener 9 digitos");
            return false;
        }else {
            editTextTelefono.setError(null);
        }

        return true;
    }
}