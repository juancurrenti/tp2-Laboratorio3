package com.example.trabajopractico2_laboratorio3.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.trabajopractico2_laboratorio3.databinding.ActivityMainBinding;
import com.example.trabajopractico2_laboratorio3.ui.registro.RegistroActivity;

public class MainActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        loginViewModel.getPasswordVisibility().observe(this, isVisible -> {
            if (isVisible) {
                binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });

        loginViewModel.getMPersona().observe(this, usuario -> {
            if (usuario != null && !usuario.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                intent.putExtra("usuarioDatos", usuario);
                startActivity(intent);
            } else {
                loginViewModel.mostrarErrorToast(getApplicationContext());
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            loginViewModel.leerObjeto(email, password);
        });

        binding.btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        binding.cbPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            loginViewModel.setPasswordVisibility(isChecked);
        });
    }
}
