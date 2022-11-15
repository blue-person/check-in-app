package com.example.check.controlador.fragmento;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.check.R;
import com.example.check.controlador.adaptador.AlbumAdapter;
import com.example.check.controlador.adaptador.ImageAdapter;
import com.example.check.repositorio.dao.ImagenDao;
import com.example.check.repositorio.entidad.Connection;
import com.example.check.repositorio.entidad.DestinosViaje;
import com.example.check.repositorio.entidad.Imagedb;
import com.example.check.servicio.utilidades.excepciones.ExcepcionConexion;
import com.example.check.servicio.utilidades.excepciones.ExcepcionTareaFB;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentoGaleria extends Fragment {
    private RecyclerView vistaReciclada;

    public FragmentoGaleria() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_social, container, false);
        vistaReciclada = vista.findViewById(R.id.view_photo);
        vistaReciclada.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        if (!new Connection(getActivity()).isConnected()) {
            throw new ExcepcionConexion("");
        } else {
            List<DestinosViaje> destinosViajes = new ArrayList<>();
            RecyclerView vistaRecicladaAlbum = vista.findViewById(R.id.view_album);
            vistaRecicladaAlbum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            AlbumAdapter adaptadorAlbum = new AlbumAdapter(destinosViajes);
            vistaRecicladaAlbum.setAdapter(adaptadorAlbum);

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = db.getReference("Expediciones");
            databaseReference.get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    throw new ExcepcionTareaFB(Objects.requireNonNull(task.getException()).getMessage());
                } else {
                    DestinosViaje destinosViaje1 = new DestinosViaje();
                    destinosViaje1.nombre = "Todas las expediciones";
                    destinosViaje1.imagen = "https://checknewplaces.com/wp-content/uploads/2021/09/Puerta-de-Orion-@ecoturismoguaviare-2.jpg";
                    destinosViajes.add(destinosViaje1);

                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        DestinosViaje destinosViaje = ds.getValue(DestinosViaje.class);
                        destinosViajes.add(destinosViaje);
                        Objects.requireNonNull(vistaRecicladaAlbum.getAdapter()).notifyDataSetChanged();
                    }
                }
            });
            Objects.requireNonNull(vistaRecicladaAlbum.getAdapter()).notifyDataSetChanged();

            vistaReciclada.setAdapter(new ImageAdapter(ImagenDao.imagedbs, getActivity()));
            FirebaseDatabase db1 = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference1 = db1.getReference("Images");

            databaseReference1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (!ImagenDao.imagedbs.contains(snapshot.getValue(Imagedb.class))) {
                        Imagedb imagedb = snapshot.getValue(Imagedb.class);
                        ImagenDao.addImage(imagedb);
                    }
                    Objects.requireNonNull(vistaReciclada.getAdapter()).notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        return vista;
    }
}