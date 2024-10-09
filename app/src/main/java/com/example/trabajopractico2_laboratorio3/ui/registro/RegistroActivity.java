package com.example.trabajopractico2_laboratorio3.ui.registro;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.trabajopractico2_laboratorio3.databinding.ActivityRegistroBinding;

public class RegistroActivity extends AppCompatActivity {
    private RegistroViewModel rm;
    private ActivityRegistroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rm = new ViewModelProvider(this).get(RegistroViewModel.class);

        rm.getUsuarioGuardado().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean resultado) {
                if (resultado) {
                    Toast.makeText(getApplicationContext(), "Datos del usuario actualizados correctamente!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Nuevo usuario registrado correctamente!", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });

        if (getIntent().hasExtra("usuarioDatos")) {
            String usuarioDatos = getIntent().getStringExtra("usuarioDatos");
            cargarDatosUsuario(usuarioDatos);
        }

        binding.btnGuardar.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            String nombre = binding.etNombre.getText().toString();
            String apellido = binding.etApellido.getText().toString();
            long dni = Long.parseLong(binding.etDni.getText().toString());

            rm.guardarActualizarUsuario(email, password, nombre, apellido, dni);
        });

        binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        binding.cbPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });
    }

    private void cargarDatosUsuario(String datos) {
        String[] partes = datos.split(" ");
        if (partes.length >= 5) {
            binding.etEmail.setText(partes[0]);
            binding.etPassword.setText(partes[1]);
            binding.etNombre.setText(partes[2]);
            binding.etApellido.setText(partes[3]);
            binding.etDni.setText(partes[4]);

            binding.etEmail.setEnabled(false);

            binding.btnGuardar.setText("Actualizar Datos");
        }
    }

}
