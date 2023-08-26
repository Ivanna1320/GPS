package com.example.gpssos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class AgregarContactoActivity extends AppCompatActivity {

    private Button regresar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        /*regresar = findViewById(R.id.regre);

        //Ir a iniciar sesion
        regresar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                perfilActivity();
            }
        });*/

    }

    /*public void perfilActivity(){
        Intent intent = new Intent(AgregarContactoActivity.this, PerfilFragment.class);
        startActivity(intent);
    }*/
}