package com.example.check.controlador.fragmento;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.check.repositorio.entidad.Connection;
import com.example.check.repositorio.entidad.Imagedb;
import com.example.check.repositorio.entidad.TravelLocation;
import com.example.check.controlador.adaptador.AlbumAdapter;
import com.example.check.repositorio.dao.GestionOfflineImage;
import com.example.check.repositorio.dao.ImagenDao;
import com.example.check.controlador.adaptador.ImageAdapter;
import com.example.check.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoGaleria#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoGaleria extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentoGaleria() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoGaleria newInstance(String param1, String param2) {
        FragmentoGaleria fragment = new FragmentoGaleria();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    private FirebaseStorage storage;
    private StorageReference reference;
    private FirebaseAuth mAuth;
    private View view;
    private RecyclerView recyclerView;
    List<Imagedb> imageList = new ArrayList<>();
    private GestionOfflineImage offlineImage;
    private File DIR_SAVE_IMAGES;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        view = inflater.inflate(R.layout.fragment_social, container, false);
        recyclerView = view.findViewById(R.id.view_photo);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        DIR_SAVE_IMAGES = new File(getActivity().getFilesDir(), "ImagePicker");
        offlineImage = new GestionOfflineImage(DIR_SAVE_IMAGES);
        System.out.println("File: " + DIR_SAVE_IMAGES.getPath());

        if (new Connection(getActivity()).isConnected()) {

            offlineImage.uploadOnline();

        }

        ImageView imageView = view.findViewById(R.id.offimg);
        if (!new Connection(getActivity()).isConnected()) {

            TextView textView = view.findViewById(R.id.offtext1);
            textView.setText("No tiene conexión a internet.");
            TextView textView2 = view.findViewById(R.id.offtext2);
            textView2.setText("Cuando tenga conexión a internet sus imagenes seran cargadas.");

            imageView.setVisibility(View.VISIBLE);

        } else {
            List<TravelLocation> travelLocations = new ArrayList<>();

            RecyclerView rvAlbum = view.findViewById(R.id.view_album);
            rvAlbum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            AlbumAdapter albumAdapter = new AlbumAdapter(travelLocations);
            rvAlbum.setAdapter(albumAdapter);


            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = db.getReference("Expediciones");
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if (!task.isSuccessful()) {
                        System.out.println("fallo");
                    } else {
                        TravelLocation travelLocation1 = new TravelLocation();
                        travelLocation1.Nombre = "Todas las expediciones";
                        travelLocation1.imagen = "https://checknewplaces.com/wp-content/uploads/2021/09/Puerta-de-Orion-@ecoturismoguaviare-2.jpg";
                        travelLocations.add(travelLocation1);
                        for (DataSnapshot ds : task.getResult().getChildren()) {

                            TravelLocation travelLocation = ds.getValue(TravelLocation.class);
                            System.out.println(travelLocation.toString());
                            travelLocations.add(travelLocation);
                            rvAlbum.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    System.out.println("Hola");
                                }
                            });
                            rvAlbum.getAdapter().notifyDataSetChanged();

                        }

                        System.out.println("AQUI???? " + String.valueOf(task.getResult().getValue()));
                    }
                }
            });

            rvAlbum.getAdapter().notifyDataSetChanged();

            recyclerView.setAdapter(new ImageAdapter(ImagenDao.imagedbs, getActivity()));
            FirebaseDatabase db1 = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference1 = db1.getReference("Images");


            databaseReference1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                    System.out.println(snapshot.getValue());
                    if (!ImagenDao.imagedbs.contains(snapshot.getValue(Imagedb.class))) {
                        Imagedb imagedb = snapshot.getValue(Imagedb.class);
                        ImagenDao.addImage(imagedb);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
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

        return view;
    }


}