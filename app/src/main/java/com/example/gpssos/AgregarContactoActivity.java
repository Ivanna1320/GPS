package com.example.gpssos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AgregarContactoActivity extends AppCompatActivity {

    private Button regresar, buscar, agregar;
    private EditText txtindentificacion;
    public TextView txtnombre, txtcelular;

    //base de datos
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        buscar = findViewById(R.id.buscar);
        agregar = findViewById(R.id.agregar);
        //txtindentificacion = findViewById(R.id.edit_id);
        //txtnombre =findViewById(R.id.texto_nombre);
        //txtcelular = findViewById(R.id.texto_celular);


        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txtindentificacion = findViewById(R.id.edit_id);
                txtnombre = findViewById(R.id.texto_nombre);
                txtcelular = findViewById(R.id.texto_celular);

                Toast.makeText(AgregarContactoActivity.this,"si funciona el boton", Toast.LENGTH_SHORT).show();
                //referencia a firrstore
                mFirestore = FirebaseFirestore.getInstance();

                String identificacion = txtindentificacion.getText().toString();

                mFirestore.collection("user").document(identificacion).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                        if (documentSnapshot.equals(txtindentificacion)) {
                            String nombreUser = documentSnapshot.getString("nombre");
                            String celularUser = documentSnapshot.getString("celular");

                            txtnombre.setText(nombreUser);
                            txtcelular.setText(celularUser);
                        }
                    }
                });
            }
        });
    }
}