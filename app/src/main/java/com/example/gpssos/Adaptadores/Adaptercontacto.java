package com.example.gpssos.Adaptadores;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpssos.R;
import com.example.gpssos.modelos.contactos;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Adaptercontacto extends FirestoreRecyclerAdapter<contactos, Adaptercontacto.ViewHolder> {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    String idContacto;
    Activity activity;
    public Adaptercontacto(@NonNull FirestoreRecyclerOptions<contactos> options,Activity activity) {
        super(options);
        this.activity = activity;

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull contactos contactos) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        idContacto = documentSnapshot.getId();
        holder.name.setText(contactos.getNombre());
        holder.surname.setText(contactos.getApellido());
        holder.celular.setText(contactos.getCelular());

        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteContacto();
            }
        });
    }

    private void DeleteContacto() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid();
        mFirestore.collection("user").document(id).collection("idContacto").document(idContacto).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Se elimino el contacto", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "no esta conectado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_contactos, parent, false);

        return new Adaptercontacto.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,surname,celular;
        Button btndelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameContac);
            surname = itemView.findViewById(R.id.lastnameContac);
            celular = itemView.findViewById(R.id.celularContac);
            btndelete = itemView.findViewById(R.id.deleteContac);

        }
    }
}
