package com.example.trabajopractico2_laboratorio3.ui.registro;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.trabajopractico2_laboratorio3.model.Usuario;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RegistroViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> usuarioGuardado;

    public RegistroViewModel(Application application) {
        super(application);
        usuarioGuardado = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getUsuarioGuardado() {
        return usuarioGuardado;
    }

    public void guardarActualizarUsuario(String mail, String password, String nombre, String apellido, long dni) {
        File archivo = new File(getApplication().getFilesDir(), "usuario.dat");
        ArrayList<Usuario> usuarios = new ArrayList<>();
        boolean usuarioExistente = false;

        if (archivo.exists()) {
            try (FileInputStream fis = new FileInputStream(archivo);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {

                while (true) {
                    try {
                        Usuario usuario = (Usuario) ois.readObject();
                        if (usuario.getMail().equals(mail)) {
                            usuario.setNombre(nombre);
                            usuario.setApellido(apellido);
                            usuario.setPassword(password);
                            usuario.setDni(dni);
                            usuarioExistente = true;
                        }
                        usuarios.add(usuario);
                    } catch (EOFException eof) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                usuarioGuardado.postValue(false);
                Toast.makeText(getApplication(), "Error al leer datos", Toast.LENGTH_LONG).show();
                return;
            }
        }

        if (!usuarioExistente) {
            usuarios.add(new Usuario(mail, password, nombre, apellido, dni));
        }

        try (FileOutputStream fos = new FileOutputStream(archivo, false);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            for (Usuario usuario : usuarios) {
                oos.writeObject(usuario);
            }

            bos.flush();
            usuarioGuardado.postValue(usuarioExistente);
        } catch (IOException e) {
            usuarioGuardado.postValue(false);
            Toast.makeText(getApplication(), "Error al guardar datos", Toast.LENGTH_LONG).show();
        }
    }
}
