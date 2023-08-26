package com.example.gpssos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class PerfilFragment extends Fragment {

    private Button aggContacto;

    public PerfilFragment() {
        // Required empty public constructor
    }


    /*public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        aggContacto = view.findViewById(R.id.agg_contac); // Asignar el botón por su ID

        aggContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
                LayoutInflater in = getLayoutInflater();
                View vi = in.inflate(R.layout.activity_agregar_contacto, null);
                alert.setView(vi);
                AlertDialog a3 = alert.create();
                a3.show();
            }
        });

        return view;
    }


}