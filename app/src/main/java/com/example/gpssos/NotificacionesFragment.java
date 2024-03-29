package com.example.gpssos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.gpssos.Adaptadores.Adapternoti;
import com.example.gpssos.modelos.notificaciones;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NotificacionesFragment extends Fragment {

    //public Button btnsolicitudes;
    RecyclerView mRecycler;
    Adapternoti mAdapter;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    //declaramos el fragment que vamos a utilizar para remplazar
    SolicitudesFragment vSolicitudes = new SolicitudesFragment();

    RelativeLayout vNoti;

    public NotificacionesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater in = getLayoutInflater();
        View vi = in.inflate(R.layout.fragment_notificaciones, container, false);
        vNoti = vi.findViewById(R.id.Contenedor_notificaciones);

        final Button btnsolicitudes = vi.findViewById(R.id.btnSolis);

        mRecycler = vi.findViewById(R.id.recyclernotificaciones);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query query =mFirestore.collection("user").document(mAuth.getUid()).collection("idNotificaciones");

        FirestoreRecyclerOptions<notificaciones> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<notificaciones>().setQuery(query, notificaciones.class).build();

        mAdapter = new Adapternoti(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

        btnsolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(vSolicitudes);
                vNoti.setVisibility(View.GONE);
            }
        });
        // Inflate the layout for this fragment
        //inflater.inflate(R.layout.fragment_notificaciones, container, false);
        return vi;

    }
    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.vistaNotificaciones,vSolicitudes);
        transaction.commitNow();

    }
}