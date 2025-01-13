package com.example.nuevomaps;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity
        extends AppCompatActivity
        implements OnMapReadyCallback
{
GoogleMap mapa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
            mapa = googleMap;
            //configurar el mapa
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);
//Mover a la Torre Eiffel
        LatLng posQuevedo = new LatLng(-1.0248850049768206, -79.4666253314406);
        CameraUpdate camUpd1 =
                CameraUpdateFactory
                        .newLatLngZoom( posQuevedo,25 );
        mapa.moveCamera(camUpd1);

        /*LatLng madrid = new LatLng(48.858057, 2.294433);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(madrid)
                .zoom(19)
                .bearing(45) //noreste arriba
                .tilt(70) //punto de vista de la cámara 70 grados
                .build();
        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);
        mapa.animateCamera(camUpd3);*/

        //Poligono
        PolylineOptions lineas = new
                PolylineOptions()
                .add(new LatLng(-0.9903233962350613, -79.42828629514277))
                .add(new LatLng(-1.0590031850166055, -79.44467825538163))
                .add(new LatLng(-1.0483357562967748, -79.50192902732154))
                .add(new LatLng(-0.9726448686549527, -79.48493455014963))
                .add(new LatLng(-0.9903233962350613, -79.42828629514277));
        lineas.width(8);
        lineas.color(Color.GREEN);
        mapa.addPolyline(lineas);
        //AGG MARCADORES AL MAPA
        mapa.addMarker(new MarkerOptions()
                .position(posQuevedo)
                .title("Centro de Quevedo"));
        mapa.addMarker(new MarkerOptions()
                .position(new LatLng(-0.9903233962350613, -79.42828629514277)));
        mapa.addMarker(new MarkerOptions()
                .position(new LatLng(-1.0590031850166055, -79.44467825538163)));
        mapa.addMarker(new MarkerOptions()
                .position(new LatLng(-1.0483357562967748, -79.50192902732154)));
        mapa.addMarker(new MarkerOptions()
                .position(new LatLng(-0.9726448686549527, -79.48493455014963)));

        //Capturar el evento click
        mapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                mapa.addMarker(new MarkerOptions()
                        .position(new LatLng(point.latitude, point.longitude)));
            }
        });
        //Pintemos un circulo que encierre quevedo
        Circle Circulo;
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(-1.0248850049768206, -79.4666253314406))
                .radius(5000) //En Metros
                .strokeColor(Color.BLACK)
                .fillColor(Color.argb(50, 150, 50, 50));
        Circulo = mapa.addCircle(circleOptions);


    }
}