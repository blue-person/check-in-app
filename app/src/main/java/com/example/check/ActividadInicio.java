package com.example.check;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.check.controlador.fragmento.Fragmento_Login;
import com.example.check.controlador.fragmento.RegistroFragmento;
import com.example.check.repositorio.dao.AutenticacionDao;
import com.example.check.repositorio.entidad.Usuario;
import com.example.check.servicio.firebase.ServicioFirebase;
import com.example.check.servicio.utilidades.dialogo.DialogoNotificacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tomlonghurst.expandablehinttext.ExpandableHintText;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;

public class ActividadInicio extends AppCompatActivity {
    private FirebaseAuth tokenAutenticacion;
    private ServicioFirebase servicioFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseAuth.getInstance().signOut();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        tokenAutenticacion = FirebaseAuth.getInstance();
        servicioFirebase = new ServicioFirebase();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) SmoothBottomBar smoothBottomBar = findViewById(R.id.log_sbar);
        remplazar(new Fragmento_Login());

        smoothBottomBar.setOnItemSelected((Function1<? super Integer, Unit>) o -> {
            if (o == 0) {
                remplazar(new Fragmento_Login());
            } else if (o == 1) {
                remplazar(new RegistroFragmento());
            }
            return null;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        autenticar();
    }

    public void remplazar(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frames, fragment);
        transaction.commit();

    }

    public void ingresar(View view) {
        TextView editTextUser = findViewById(R.id.user);
        TextView editTextPass = findViewById(R.id.pass);
        String user = editTextUser.getText().toString();
        String pass = editTextPass.getText().toString();

        if (!user.equals("") && !pass.equals("")) {
            tokenAutenticacion.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    autenticar();
                } else {
                    DialogoNotificacion dialogoNotificacion = new DialogoNotificacion(ActividadInicio.this);
                    View box = LayoutInflater.from(getApplicationContext()).inflate(R.layout.box_notification, findViewById(R.id.dialog_notification));
                    dialogoNotificacion.dispararDialogo(box, getString(R.string.error_titulo_credenciales), getString(R.string.error_info_credenciales), R.raw.fail);
                }
            });
        } else {
            DialogoNotificacion dialogoNotificacion = new DialogoNotificacion(ActividadInicio.this);
            View box = LayoutInflater.from(getApplicationContext()).inflate(R.layout.box_notification, findViewById(R.id.dialog_notification));
            dialogoNotificacion.dispararDialogo(box, getString(R.string.error_titulo_credenciales), getString(R.string.error_info_credenciales), R.raw.fail);
        }
    }

    private void autenticar() {
        FirebaseUser currentUser = tokenAutenticacion.getCurrentUser();
        if (currentUser != null) {
            Intent log = new Intent(this, ActividadPrincipal.class);
            startActivity(log);
        }
    }

    public void registrar(View view) {
        ExpandableHintText editTextUser = findViewById(R.id.regNombre);
        ExpandableHintText editTextMail = findViewById(R.id.regCorreo);
        ExpandableHintText editTextPass = findViewById(R.id.regPass);
        ExpandableHintText editTextPassConf = findViewById(R.id.regPassConf);

        String user = editTextUser.getText();
        String mail = editTextMail.getText();
        String pass = editTextPass.getText();
        String passConf = editTextPassConf.getText();
        boolean usuarioValido = new AutenticacionDao().esValido(user, mail, pass, passConf);

        if (!user.equals("") && !pass.equals("") && Boolean.TRUE.equals(usuarioValido)) {
            tokenAutenticacion.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    autenticar();
                    Usuario user1 = new Usuario();
                    user1.setNombre(editTextUser.getText());
                    user1.setCorreo(editTextMail.getText());
                    user1.setExpedicion("NA");
                    user1.setImagen("");
                    servicioFirebase.registrarUsuario(user1, task);
                } else {
                    DialogoNotificacion dialogoNotificacion = new DialogoNotificacion(ActividadInicio.this);
                    View box = LayoutInflater.from(getApplicationContext()).inflate(R.layout.box_notification, findViewById(R.id.dialog_notification));
                    dialogoNotificacion.dispararDialogo(box, getString(R.string.error_titulo_credenciales), getString(R.string.error_info_credenciales), R.raw.fail);
                }
            });
        } else {
            DialogoNotificacion dialogoNotificacion = new DialogoNotificacion(ActividadInicio.this);
            View box = LayoutInflater.from(getApplicationContext()).inflate(R.layout.box_notification, findViewById(R.id.dialog_notification));
            dialogoNotificacion.dispararDialogo(box, getString(R.string.error_titulo_credenciales), getString(R.string.error_info_credenciales), R.raw.fail);
        }
    }
}