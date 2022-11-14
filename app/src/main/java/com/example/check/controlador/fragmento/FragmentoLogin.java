package com.example.check.controlador.fragmento;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.check.R;

<<<<<<< Updated upstream:app/src/main/java/com/example/check/controlador/fragmento/Fragmento_Login.java
public class Fragmento_Login extends Fragment {
=======
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoLogin extends Fragment {
>>>>>>> Stashed changes:app/src/main/java/com/example/check/controlador/fragmento/FragmentoLogin.java

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public FragmentoLogin() {
        // Required empty public constructor
    }

<<<<<<< Updated upstream:app/src/main/java/com/example/check/controlador/fragmento/Fragmento_Login.java
=======
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragmento_Login.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoLogin newInstance(String param1, String param2) {
        FragmentoLogin fragment = new FragmentoLogin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
>>>>>>> Stashed changes:app/src/main/java/com/example/check/controlador/fragmento/FragmentoLogin.java



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);


        return view;
    }

}