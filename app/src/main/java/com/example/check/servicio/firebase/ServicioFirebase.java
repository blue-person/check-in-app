package com.example.check.servicio.firebase;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.example.check.R;
import com.example.check.repositorio.entidad.Imagedb;
import com.example.check.repositorio.entidad.Usuario;
import com.example.check.servicio.utilidades.Constantes;
import com.example.check.servicio.utilidades.dialogo.DialogoCarga;
import com.example.check.servicio.utilidades.dialogo.DialogoNotificacion;
import com.example.check.servicio.utilidades.excepciones.ExcepcionTareaFB;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Formatter;
import java.util.Objects;
import java.util.UUID;

public class ServicioFirebase {
    private FirebaseAuth tokenAutenticacion;
    private FirebaseStorage instanciaAlmacenamiento;
    private StorageReference referenciaAlmacenamiento;

    public ServicioFirebase() {
        tokenAutenticacion = FirebaseAuth.getInstance();
        instanciaAlmacenamiento = FirebaseStorage.getInstance();
        referenciaAlmacenamiento = instanciaAlmacenamiento.getReference();
    }

    public FirebaseAuth getTokenAutenticacion() {
        return tokenAutenticacion;
    }

    public void setTokenAutenticacion(FirebaseAuth tokenAutenticacion) {
        this.tokenAutenticacion = tokenAutenticacion;
    }

    public FirebaseStorage getInstanciaAlmacenamiento() {
        return instanciaAlmacenamiento;
    }

    public void setInstanciaAlmacenamiento(FirebaseStorage instanciaAlmacenamiento) {
        this.instanciaAlmacenamiento = instanciaAlmacenamiento;
    }

    public StorageReference getReference() {
        return referenciaAlmacenamiento;
    }

    public void setReference(StorageReference referenciaAlmacenamiento) {
        this.referenciaAlmacenamiento = referenciaAlmacenamiento;
    }

    public void subirFoto(Uri filePath, Context context, View box, View box2) {
        String tokenUsuario = getTokenAutenticacion().getUid();
        String idFoto = UUID.randomUUID().toString();
        String urlReferencia = String.format("images/%s/%s", tokenUsuario, idFoto);
        StorageReference ref = getReference().child(urlReferencia);

        DialogoCarga dialogoCarga = new DialogoCarga(context);
        dialogoCarga.dispararDialogo(box);

        ref.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri uriImage = uriTask.getResult();
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    DocumentReference docRef = database.collection(Constantes.KEY_COLLECTION_USERS).document(Objects.requireNonNull(getTokenAutenticacion().getUid()));
                    docRef.get().addOnSuccessListener(documentSnapshot -> {
                        Usuario usuario = documentSnapshot.toObject(Usuario.class);
                        Imagedb imdb = new Imagedb(Timestamp.now().toString(), usuario.getNombre(), uriImage.toString(), usuario.getExpedicion());
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = db.getReference("Images");
                        databaseReference.push().setValue(imdb);
                        dialogoCarga.finalizar();

                        DialogoNotificacion dialogoNotificacion = new DialogoNotificacion(context);
                        dialogoNotificacion.dispararDialogo(box2, "Notificacion", "La foto se actualizo correctamente", R.raw.ok);
                    });
                })

                .addOnFailureListener(e -> {
                    throw new ExcepcionTareaFB("No fue posible actualizar la foto");
                }).addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    Formatter formatter = new Formatter();
                    formatter.format("%.0f", progress);
                    dialogoCarga.porsentaje(formatter);

                });
    }

    public void registrarUsuario(Usuario user, Task<AuthResult> task) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constantes.KEY_COLLECTION_USERS).document(Objects.requireNonNull(task.getResult().getUser()).getUid())
                .set(user);
    }
}
