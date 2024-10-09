package com.example.trabajopractico2_laboratorio3.ui.login;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.trabajopractico2_laboratorio3.model.Usuario;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<String> mUsuario;
    private MutableLiveData<Boolean> passwordVisibility;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mUsuario = new MutableLiveData<>();
        passwordVisibility = new MutableLiveData<>();
    }

    public LiveData<Boolean> getPasswordVisibility() {
        return passwordVisibility;
    }

    public void setPasswordVisibility(boolean isVisible) {
        passwordVisibility.setValue(isVisible);
    }

    public LiveData<String> getMPersona() {
        return mUsuario;
    }

    public void leerObjeto(String email, String password) {
        File archivo = new File(getApplication().getFilesDir(), "usuario.dat");
        boolean usuarioEncontrado = false;
        StringBuilder sb = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(archivo);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            while (true) {
                try {
                    Usuario user = (Usuario) ois.readObject();
                    if (user.getMail().equals(email) && user.getPassword().equals(password)) {
                        sb.append(user.getMail()).append(" ")
                                .append(user.getPassword()).append(" ")
                                .append(user.getNombre()).append(" ")
                                .append(user.getApellido()).append(" ")
                                .append(user.getDni()).append("\n");
                        usuarioEncontrado = true;
                        break;
                    }
                } catch (EOFException eof) {
                    break;
                }
            }

            if (usuarioEncontrado) {
                mUsuario.setValue(sb.toString().trim());
            } else {
                mUsuario.setValue("");
            }

        } catch (Exception e) {
            mUsuario.setValue("");
        }
    }

    public void mostrarErrorToast(Context context) {
        Toast.makeText(context, "Usuario no registrado o contraseña incorrecta.", Toast.LENGTH_LONG).show();
    }
}
