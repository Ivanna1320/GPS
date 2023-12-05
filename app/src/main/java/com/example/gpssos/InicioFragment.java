package com.example.gpssos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class InicioFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationClient;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;

    private boolean isButtonClickable = true;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private static final int VOLUME_UP = KeyEvent.KEYCODE_VOLUME_UP;
    private static final int REQUIRED_CLICKS = 3;
    private int volumeUpCount = 0;

    String idContacto,nombreUser;
    Double latitud, longitud;

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        final Button btnCirculo = view.findViewById(R.id.btnCirculo);
        getlocalizacion();
        // Detecta eventos de teclas

        //Declarar la base de datos para guardar los datos
        mFirestore = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        subirUbicacion();

        btnCirculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getCargarlocalizacion();
                subirUbicacion();
                GuardarDatos();
            }
        });
        // Inflate the layout for this fragment

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getWindow().getDecorView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == VOLUME_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
                    handleVolumeUpClick();
                    return true;
                }
                return false;
            }
        });
    }
    private void handleVolumeUpClick() {
        volumeUpCount++; // Incrementa el contador cuando se presiona el botón de subir volumen
        if (volumeUpCount == REQUIRED_CLICKS) {
            // Acción a realizar después de tres clics en el botón de subir volumen
            // Por ejemplo, mostrar un mensaje o ejecutar alguna otra lógica
            GuardarDatos();
            Toast.makeText(getActivity(), "¡se mando la ubicaicion!", Toast.LENGTH_SHORT).show();

            // Restablece el contador después de realizar la acción
            volumeUpCount = 0;
        }
    }


    private void subirUbicacion() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                }
            }
        });
    }

    private void GuardarDatos() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String idUsuario = user.getUid();
        mFirestore.collection("user").document(idUsuario).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    nombreUser = documentSnapshot.getString("nombre");
                }
            }
        });

        // Crear un mapa con los datos de latitud y longitud
        Map<String, Object> ubicacion = new HashMap<>();
        ubicacion.put("id",idUsuario);
        ubicacion.put("nombre",nombreUser);
        ubicacion.put("latitud", latitud);
        ubicacion.put("longitud", longitud);


        mFirestore.collection("user").document(idUsuario).collection("idContacto").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        idContacto = documentSnapshot.getId();
                        String idContac = documentSnapshot.getString("id");
                        mFirestore.collection("user").document(idContac).collection("idUbicaciones").document(idUsuario).set(ubicacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(),"se mando la ubicaciones exitosamente", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /*private void getCargarlocalizacion() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener =new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latitud = ""+ location.getLatitude();
                longitud= ""+ location.getLongitude();
            }
        };
        int permiso = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, locationListener);
        Toast.makeText(getActivity(), "Ubicacion generada exitosamente", Toast.LENGTH_SHORT).show();
    }*/
    private void getlocalizacion() {
        int permiso = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permiso == PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){

            }else {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
    }
}