package com.example.gpssos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.Executor;


public class PerfilFragment extends Fragment {

    private Button aggContacto, btnCerrar;
    private TextView txtnameUser, txtidentificadorUser, txtEmailUser, txtCelularUser;
    private ImageButton btncopy;
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

        btncopy = view.findViewById(R.id.btncopy);

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

        //boton para abrir la pantalla para agregar un contacto
        aggContacto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
                LayoutInflater in = getLayoutInflater();
                View vi = in.inflate(R.layout.activity_agregar_contacto, null);
                final EditText txtidentificacion = vi.findViewById(R.id.edit_id);
                final TextView txtnombre = vi.findViewById(R.id.texto_nombre);
                final TextView txtcelular = vi.findViewById(R.id.texto_celular);
                final Button buscar = vi.findViewById(R.id.buscar);
                final Button agregar = vi.findViewById(R.id.buscar);


                alert.setView(vi);
                AlertDialog a3 = alert.create();
                a3.show();

                buscar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String myId = txtidentificadorUser.getText().toString();
                        String searchName = txtidentificacion.getText().toString();
                        if (myId.equals(searchName)){

                            Toast.makeText(getActivity(), "No puedes buscar tu misma Id", Toast.LENGTH_SHORT).show();

                        }else {
                            mFirestore.collection("user").whereEqualTo("identificacion",searchName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    //lo mas complicado de hacer
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                                        String userName = document.getString("nombre");
                                        String userPhone = document.getString("celular");

                                        //campos que estan dentro del dialog alert
                                        txtnombre.setText(userName);
                                        txtcelular.setText(userPhone);

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    }
                });
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
        //boton paraq copiar el id del usuario
        btncopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence textToCopy = txtidentificadorUser.getText();
                copyToClipboard(textToCopy);

            }
        });

        return view;
    }
    private void copyToClipboard(CharSequence text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Texto Copiado", text);
        Toast.makeText(getActivity(), "Id copiado", Toast.LENGTH_SHORT).show();
        clipboard.setPrimaryClip(clip);
    }
}