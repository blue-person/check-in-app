package com.example.check;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import android.content.Intent;

import androidx.annotation.NonNull;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GestionExpediciones gesExp;
    private FirebaseStorage storage;
    private StorageReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        gesExp = new GestionExpediciones();
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        Connection connection = new Connection();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SmoothBottomBar smoothBottomBar = findViewById(R.id.bar_nav);
        System.out.println(connection.isConnected() + "------------");
        replace(new HomeFragment());


        smoothBottomBar.setOnItemSelected((Function1<? super Integer, kotlin.Unit>) o -> {

            System.out.println(o);

            switch (o) {
                case 0:
                    replace(new HomeFragment());
                    break;
                case 1:
                    replace(new SocialFragment());
                    break;
                case 2:
                    replace(new ChatFragment());
                    break;
                case 3:
                    replace(new UserFragment());
                    break;
            }


            return null;
        });

        getAllCloudImage();


    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frames, fragment);
        transaction.commit();

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (currentUser == null) {
            goLogin();
        }
    }

    private void goLogin() {


        Intent Log = new Intent(this, LoginActivity.class);
        startActivity(Log);

    }

    private void goWeb(String inURL) {

        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));

        startActivity(browse);

    }

    public void logout(View view) {
        mAuth.signOut();
        goLogin();
    }

    public void getView(View view) {

        KenBurnsView kbvImage;
        TextView textTitle, textLocation, textStartRating;

        System.out.println(view.getTag());

        kbvImage = view.findViewById(R.id.kbvLocation);
        textTitle = view.findViewById(R.id.textTitle);
        textStartRating = view.findViewById(R.id.textStartRating);
        textLocation = view.findViewById(R.id.textLocation);

        System.out.println(textTitle.getText());
        System.out.println(textStartRating.getText());
        System.out.println(textLocation.getText());
        System.out.println(kbvImage.getTag());

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this, R.style.bt_sheet_dialog
        );

        View bottonSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bt_sheet, (LinearLayout) findViewById(R.id.bt_sheet_container)
        );
        bottonSheetView.findViewById(R.id.button_reserva).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                goWeb((String) textTitle.getTag());

                bottomSheetDialog.dismiss();
            }
        });

        KenBurnsView kenBurnsView = bottonSheetView.findViewById(R.id.imageRes);
        TextView textViewexp = bottonSheetView.findViewById(R.id.expedicion);
        TextView textViewlugar = bottonSheetView.findViewById(R.id.lugar);

        bottonSheetView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewe) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Haz parte de nuestras " +
                        "expediciones programadas a distintos lugares de Colombia. " +
                        "Una diversidad de destinos, itinerarios llenos de momentos" +
                        " auténticos y experiencias de vida que recordarás para siempre " + textTitle.getTag());
                try {
                    MainActivity.this.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    System.out.println("Whatsapp have not been installed.");
                }
            }
        });

        textViewexp.setText(textTitle.getText());
        textViewlugar.setText(textLocation.getText());
        Picasso.get().load((String) kbvImage.getTag()).into(kenBurnsView);
        bottomSheetDialog.setContentView(bottonSheetView);
        bottomSheetDialog.show();

    }

    public void showImageOp(View view) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.bt_sheet_dialog);

        View bottonSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bt_image, (LinearLayout) findViewById(R.id.bt_image_container)
        );

        bottonSheetView.findViewById(R.id.cameraOp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromCamera();
                bottomSheetDialog.dismiss();
            }
        });
        bottonSheetView.findViewById(R.id.filesOp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromFiles();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottonSheetView);
        bottomSheetDialog.show();

    }

    public void fromFiles() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    private void fromCamera() {

        ImagePicker.with(this)
                .cameraOnly()    //User can only capture image using Camera
                .start(REQUEST_IMAGE_CAPTURE);

    }

    public static final int PICK_IMAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode){
            case 1:

                Uri currentUri = data.getData();
                System.out.println(currentUri);
                System.out.println(requestCode);

                View box = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.dialog_box, findViewById(R.id.dialog_box_con)
                );

                ImageView imageView = box.findViewById(R.id.selectedimage);
                Picasso.get().load(currentUri).into(imageView);
                Dialog dialog = new Dialog(this);

                box.findViewById(R.id.azul).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadImage(currentUri);
                        dialog.dismiss();
                    }
                });
                box.findViewById(R.id.rojo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });

                dialog.setContentView(box);
                dialog.show();
        }

    }


    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref
                    = reference
                    .child(
                            "images/"
                                    + mAuth.getUid() + "/" + Math.random());

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {


                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(MainActivity.this,
                                                    "Imagen Actualizada!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(MainActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }

    public List<Image> getAllCloudImage(){
        List<Image> imageList = new ArrayList<>();
        StorageReference listRef = storage.getReference().child("images/"
                + mAuth.getUid());
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference item : listResult.getItems()) {
                            System.out.println("222");
                            System.out.println(item.getPath());
                            System.out.println(item.getBucket());
                            System.out.println(item.getDownloadUrl());
                            System.out.println(item.getRoot());

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
        return imageList;
    }


}