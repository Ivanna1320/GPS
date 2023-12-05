package com.example.gpssos.Adaptadores;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpssos.R;
import com.example.gpssos.modelos.solicitudes;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Adaptersolis extends FirestoreRecyclerAdapter<solicitudes, Adaptersolis.ViewHolder> {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    String  idSolicitud;
    TextView txtnomsoli;
    String nombreContacto,apellidoContacto,celularContacto;

    Activity activity;
    //FragmentManager fm;
    public Adaptersolis(@NonNull FirestoreRecyclerOptions<solicitudes> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull Adaptersolis.ViewHolder holder, int position, @NonNull solicitudes solicitudes) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        idSolicitud = documentSnapshot.getId();
        nombreContacto = documentSnapshot.getString("name");
        apellidoContacto = documentSnapshot.getString("surname");
        celularContacto = documentSnapshot.getString("celular");

        holder.name.setText(solicitudes.getName());
        holder.surname.setText(solicitudes.getSurname());
        holder.celular.setText(solicitudes.getCelular());

        //datos para el AlertDialog

        holder.btnVerCorfirmacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(activity, ConfirmacionActivity.class);
                i.putExtra("idSolicitud",idSolicitud);
                activity.startActivity(i);*/
                MostrarAlert(v.getContext());

            }
        });
    }
    private void MostrarAlert(Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater in = LayoutInflater.from(context);
        View vi = in.inflate(R.layout.activity_confirmacion, null);
        final Button Aceptar = vi.findViewById(R.id.aceptar);
        final Button Rechazar = vi.findViewById(R.id.rechazar);
        txtnomsoli = vi.findViewById(R.id.textname_corfirmacion);
        txtnomsoli.setText(nombreContacto);

        alert.setView(vi);
        AlertDialog a3 = alert.create();
        a3.show();

        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarContacto();

                DeleteSolicitud();
                Toast.makeText(activity, "Solicitud Aceptada", Toast.LENGTH_SHORT).show();
            }
        });

        Rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteSolicitud();
                Toast.makeText(activity, "Solicitud eliminada", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void DeleteSolicitud() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid();

        mFirestore.collection("user").document(id).collection("idSolicitudes").document(idSolicitud).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "no esta conectado", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void AgregarContacto() {
        //String pa que jalar el nombre del usuario
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("id", idSolicitud);
        map.put("nombre",nombreContacto);
        map.put("apellido",apellidoContacto);
        map.put("celular",celularContacto);
        mFirestore.collection("user").document(id).collection("idContacto").document(idSolicitud).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(activity, "no esta conectado", Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public Adaptersolis.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_solicitudes, parent, false);

        return new Adaptersolis.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,surname,celular;
        Button btnVerCorfirmacion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nametxt);
            surname = itemView.findViewById(R.id.lastnametxt);
            celular = itemView.findViewById(R.id.celular);
            btnVerCorfirmacion = itemView.findViewById(R.id.btnVerSolis);
        }
    }
}
