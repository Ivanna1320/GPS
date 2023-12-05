package com.example.gpssos.Adaptadores;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpssos.R;
import com.example.gpssos.modelos.notificaciones;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class Adapternoti extends FirestoreRecyclerAdapter<notificaciones, Adapternoti.ViewHolder> {


    public Adapternoti(@NonNull FirestoreRecyclerOptions<notificaciones> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Adapternoti.ViewHolder holder, int position, @NonNull notificaciones notificaciones) {

        holder.name.setText(notificaciones.getName());

    }

    @NonNull
    @Override
    public Adapternoti.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificacion_solicitud, parent, false);
        return new Adapternoti.ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nametxt);
        }
    }
}
