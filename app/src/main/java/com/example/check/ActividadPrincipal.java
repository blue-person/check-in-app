package com.example.check;

import static io.grpc.okhttp.internal.Platform.logger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.check.controlador.adaptador.ImageAdapter;
import com.example.check.controlador.fragmento.FragmentoGaleria;
import com.example.check.controlador.fragmento.FragmentoInicio;
import com.example.check.controlador.fragmento.FragmentoPerfil;
import com.example.check.repositorio.dao.ImagenDao;
import com.example.check.repositorio.entidad.Connection;
import com.example.check.repositorio.entidad.Imagedb;
import com.example.check.repositorio.entidad.Usuario;
import com.example.check.servicio.firebase.ServicioFirebase;
import com.example.check.servicio.utilidades.Constantes;
import com.example.check.servicio.utilidades.excepciones.ExcepcionConexion;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActividadPrincipal extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ServicioFirebase servicioFirebase;
    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connection = new Connection(this);

        ImageView imageTopView = findViewById(R.id.topImg);
        TextView textTopView = findViewById(R.id.topText);

        LinearLayout index0 = findViewById(R.id.index0);
        LinearLayout index1 = findViewById(R.id.index1);
        LinearLayout index2 = findViewById(R.id.index2);

        remplazar(new FragmentoInicio());

        index0.setOnClickListener(view -> {
            remplazar(new FragmentoInicio());
            imageTopView.setImageResource(R.drawable.ic_location);
            textTopView.setText(R.string.mensaje_inicio);
        });

        index1.setOnClickListener(view -> {
            remplazar(new FragmentoGaleria());
            imageTopView.setImageResource(R.drawable.ic_baseline_favorite_24);
            textTopView.setText(R.string.mensaje_galeria);
        });

        index2.setOnClickListener(view -> {
            remplazar(new FragmentoPerfil());
            imageTopView.setImageResource(R.drawable.ic_user);
            textTopView.setText(R.string.mensaje_perfil);
        });

    }

    public void remplazar(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frames, fragment);
        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void cambiarIntento(String accion, String contexto) {
        Intent log = new Intent(accion, Uri.parse(contexto));
        startActivity(log);
    }

    public void desplegarInformacion(View view) {
        if (!connection.isConnected()) {
            throw new ExcepcionConexion(connection.toString());
        } else {
            KenBurnsView kbvImage = view.findViewById(R.id.kbvLocation);
            TextView textTitle = view.findViewById(R.id.textTitle);
            TextView textLocation = view.findViewById(R.id.textLocation);

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActividadPrincipal.this, R.style.bt_sheet_dialog);

            View bottonSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bt_sheet, findViewById(R.id.bt_sheet_container));
            bottonSheetView.findViewById(R.id.button_reserva).setOnClickListener(view1 -> {
                cambiarIntento(Intent.ACTION_VIEW, (String) textTitle.getTag());
                bottomSheetDialog.dismiss();
            });

            KenBurnsView kenBurnsView = bottonSheetView.findViewById(R.id.imageRes);
            TextView textViewexp = bottonSheetView.findViewById(R.id.expedicion);
            TextView textViewlugar = bottonSheetView.findViewById(R.id.lugar);

            bottonSheetView.findViewById(R.id.share).setOnClickListener(viewe -> {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Haz parte de nuestras " + "expediciones programadas a distintos lugares de Colombia. " + "Una diversidad de destinos, itinerarios llenos de momentos" + " aut??nticos y experiencias de vida que recordar??s para siempre " + textTitle.getTag());
                try {
                    ActividadPrincipal.this.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    logger.info("El usuario no tiene instalado Whatsapp");
                }
            });

            bottonSheetView.findViewById(R.id.set).setOnClickListener(viewe -> {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection(Constantes.KEY_COLLECTION_USERS).document(Objects.requireNonNull(servicioFirebase.getTokenAutenticacion().getUid())).get().addOnSuccessListener(documentSnapshot -> {
                    bottomSheetDialog.dismiss();
                    Usuario user = documentSnapshot.toObject(Usuario.class);
                    assert user != null;
                    user.setExpedicion(kbvImage.getTag().toString());
                    database.collection(Constantes.KEY_COLLECTION_USERS).document(servicioFirebase.getTokenAutenticacion().getUid()).set(user);
                });
            });

            textViewexp.setText(textTitle.getText());
            textViewlugar.setText(textLocation.getText());
            Picasso.get().load(textLocation.getTag().toString()).into(kenBurnsView);
            bottomSheetDialog.setContentView(bottonSheetView);
            bottomSheetDialog.show();
        }
    }

    public void desplegarOpcionesdeCaptura(View view) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActividadPrincipal.this, R.style.bt_sheet_dialog);
        View bottonSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bt_image, findViewById(R.id.bt_image_container));

        bottonSheetView.findViewById(R.id.cameraOp).setOnClickListener(view1 -> {
            seleccionarDeCamara();
            bottomSheetDialog.dismiss();
        });
        bottonSheetView.findViewById(R.id.filesOp).setOnClickListener(view12 -> {
            seleccionarDeArchivos();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottonSheetView);
        bottomSheetDialog.show();
    }

    public void seleccionarDeArchivos() {
        ImagePicker.with(this).galleryOnly().crop().start(REQUEST_IMAGE_CAPTURE);

    }

    private void seleccionarDeCamara() {
        ImagePicker.with(this).cameraOnly().start(REQUEST_IMAGE_CAPTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == 1) {
            assert data != null;
            Uri currentUri = data.getData();

            View box = LayoutInflater.from(getApplicationContext()).inflate(R.layout.box_confirmacion, findViewById(R.id.dialog_box));
            Dialog dialog = new Dialog(this);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = box.findViewById(R.id.textoPrincipal);
            textView.setText(R.string.alerta_titulo_confirmacion_foto);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textinfoView = box.findViewById(R.id.info);
            textinfoView.setText(R.string.alerta_info_confirmacion_foto);
            LottieAnimationView imageView = box.findViewById(R.id.img);
            imageView.setAnimation(R.raw.request);

            box.findViewById(R.id.boton_de_confirmacion).setOnClickListener(view -> {
                dialog.dismiss();
                subirImagen(currentUri);
            });
            box.findViewById(R.id.boton_de_cancelar).setOnClickListener(view2 -> dialog.dismiss());
            dialog.setContentView(box);
            dialog.show();
        }
    }

    private void subirImagen(Uri filePath) {
        if (!connection.isConnected()) {
            return;
        }
        if (filePath != null) {
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View box2 = LayoutInflater.from(this).inflate(R.layout.box_notification, findViewById(R.id.dialog_notification));
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View box = LayoutInflater.from(this).inflate(R.layout.box_carga, findViewById(R.id.load_box));
            servicioFirebase.subirFoto(filePath, this, box, box2);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filtrar(View view) {
        RecyclerView recyclerView = findViewById(R.id.view_photo);
        List<Imagedb> imagedbs = new ArrayList<>(ImagenDao.imagedbs);
        imagedbs.clear();

        String tag = view.getTag().toString();
        if (tag.equals(getString(R.string.titulo_todas_expediciones))) {
            imagedbs.addAll(ImagenDao.imagedbs);
        } else {
            for (Imagedb item : ImagenDao.imagedbs) {
                if (item.getExpedicion().equals(tag)) {
                    imagedbs.add(item);
                }
            }
        }
        recyclerView.setAdapter(new ImageAdapter(imagedbs, this));
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
    }
}