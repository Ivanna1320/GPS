package com.example.gpssos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.concurrent.Executor;


public class PerfilFragment extends Fragment {

    private Button aggContacto, btnCerrar;
    private TextView txtnameUser, txtidentificadorUser, txtEmailUser, txtCelularUser;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;


    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        //UserId = mAuth.getCurrentUser().getUid();
        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid();

        aggContacto = view.findViewById(R.id.agg_contac); // Asignar el botón por su ID
        btnCerrar = view.findViewById(R.id.btn_cerrar);
        txtnameUser = view.findViewById(R.id.textname);
        txtidentificadorUser = view.findViewById(R.id.textid);
        txtEmailUser = view.findViewById(R.id.txt_email);
        txtCelularUser = view.findViewById(R.id.txt_cel);

        txtEmailUser.setText(mAuth.getCurrentUser().getEmail());

        mFirestore.collection("user").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    String nombreUser = documentSnapshot.getString("nombre");
                    String identificadorUser = documentSnapshot.getString("identificacion");
                    String emailUser = documentSnapshot.getString("email");
                    String celularUser = documentSnapshot.getString("celular");

                    txtnameUser.setText(nombreUser);
                    txtidentificadorUser.setText(identificadorUser);
                    txtEmailUser.setText(emailUser);
                    txtCelularUser.setText(celularUser);
                }
            }
        });

        /*mFirestore.collection("user").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String nombreUser = documentSnapshot.getString("nombre");
                    String identificadorUser = documentSnapshot.getString("identificacion");
                    String emailUser = documentSnapshot.getString("email");
                    String celularUser = documentSnapshot.getString("celular");

                    txtnameUser.setText(nombreUser);
                    txtidentificadorUser.setText(identificadorUser);
                    txtEmailUser.setText(emailUser);
                    txtCelularUser.setText(celularUser);
                }
            }
        });*/
        /*documentReference.addSnapshotListener((Executor) this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                nameUser.setText(documentSnapshot.getString("nombre"));
                identidicadorUser.setText(documentSnapshot.getString("identificacion"));
                txtEmailUser.setText(documentSnapshot.getString("email"));
                txtCelularUser.setText(documentSnapshot.getString("celular"));
            }
        });*/

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

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }


}