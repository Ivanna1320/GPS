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
import android.widget.RelativeLayout;

import com.example.gpssos.Adaptadores.Adaptersolis;
import com.example.gpssos.modelos.solicitudes;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SolicitudesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SolicitudesFragment extends Fragment {

    RecyclerView mRecycler;
    Adaptersolis mAdapter;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    public SolicitudesFragment() {
        // Required empty public constructor
    }

    public static SolicitudesFragment newInstance(String param1, String param2) {
        SolicitudesFragment fragment = new SolicitudesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater in = getLayoutInflater();
        View vi = in.inflate(R.layout.fragment_solicitudes, container, false);

        final Button btnNotificaciones = vi.findViewById(R.id.btnNoti);

        mRecycler = vi.findViewById(R.id.recyclersolicitudes);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query query =mFirestore.collection("user").document(mAuth.getUid()).collection("idSolicitudes");

        FirestoreRecyclerOptions<solicitudes> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<solicitudes>().setQuery(query, solicitudes.class).build();

        mAdapter = new Adaptersolis(firestoreRecyclerOptions,getActivity());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

        btnNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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