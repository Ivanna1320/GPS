package com.example.gpssos;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gpssos.Adaptadores.Adaptercontacto;
import com.example.gpssos.Adaptadores.Adaptersolis;
import com.example.gpssos.modelos.contactos;
import com.example.gpssos.modelos.solicitudes;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class fragment_contactos extends Fragment {

    RecyclerView mRecycler;
    Adaptercontacto mAdapter;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    public fragment_contactos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater in = getLayoutInflater();
        View vi = in.inflate(R.layout.fragment_contactos, container, false);

        //final Button btnNotificaciones = vi.findViewById(R.id.btnNoti);

        mRecycler = vi.findViewById(R.id.recyclercontactos);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query query =mFirestore.collection("user").document(mAuth.getUid()).collection("idContacto");

        FirestoreRecyclerOptions<contactos> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<contactos>().setQuery(query, contactos.class).build();

        mAdapter = new Adaptercontacto(firestoreRecyclerOptions,getActivity());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

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

}