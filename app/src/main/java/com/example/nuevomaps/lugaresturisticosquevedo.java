package com.example.nuevomaps;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;

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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import WebServices.Asynchtask;
import WebServices.WebService;

public class lugaresturisticosquevedo
        extends AppCompatActivity
    implements OnMapReadyCallback, Asynchtask
         {
             GoogleMap mapa;
             Double lat, lng;
             float radio;
             Circle circulo = null;

             Slider sliderRadio;
             EditText txtLatitud, txtLongitud;
             ArrayList<Marker> markers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lugaresturisticosquevedo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lat = -1.02313;  lng = -79.459561;       radio = 1;

        txtLatitud = findViewById(R.id.txtlatitud);
        txtLongitud = findViewById(R.id.txtlongitud);
        sliderRadio = findViewById(R.id.sliderRadio);
        sliderRadio.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                radio = slider.getValue();
                updateInterfaz();
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });

    }

             @Override
             public void onMapReady(@NonNull GoogleMap googleMap) {
                 mapa=googleMap;
                 //configurar el mapa
                 mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                 mapa.getUiSettings().setZoomControlsEnabled(true);
//Mover a la Torre Eiffel
                 LatLng posQuevedo = new LatLng(-1.0248850049768206, -79.4666253314406);
                 CameraUpdate camUpd1 =
                         CameraUpdateFactory
                                 .newLatLngZoom( posQuevedo,17 );
                 mapa.moveCamera(camUpd1);

                 mapa.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                     @Override
                     public void onCameraIdle() {
                         LatLng center = mapa.getCameraPosition().target;
                         lat = center.latitude;
                         lng =center.longitude;
                         updateInterfaz();

                     }
                 });
                 mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                     @Override
                     public boolean onMarkerClick(Marker marker) {
                         marker.showInfoWindow();
                         return true;
                     }
                 });

             }


             private void updateInterfaz(){
                 txtLatitud.setText(String.format("%.4f", lat));
                 txtLongitud.setText(String.format("%.4f", lng));

                 if(circulo!=null){  circulo.remove();  circulo = null; }

                 CircleOptions circleOptions = new CircleOptions()
                         .center(new LatLng(lat,lng))
                         .radius(radio * 100)
                         .strokeColor(Color.RED)
                         .fillColor(Color.argb(50, 150, 50, 50));

                 circulo = mapa.addCircle(circleOptions);
                 Map<String, String> datos = new HashMap<String, String>();
                 WebService ws= new WebService(
                         "https://turismoquevedo.com/lugar_turistico/json_getlistadoMapa?lat=" + lat +   "&lng=" + lng +"&radio=" + (radio/10.0)  ,datos,

                         lugaresturisticosquevedo.this, lugaresturisticosquevedo.this);
                 ws.execute("GET");

             }

             @Override
             public void processFinish(String result) throws JSONException {
                 for (Marker marker : markers) marker.remove();
                 markers.clear();

                 JSONObject JSONobj= new JSONObject(result);
                 JSONArray jsonLista = JSONobj.getJSONArray("data");
                 for(int i=0; i< jsonLista.length(); i++){
                     JSONObject lugar= jsonLista.getJSONObject(i);
                     markers.add(mapa.addMarker(
                             new MarkerOptions().position(
                                     new LatLng(lugar.getDouble("lat"), lugar.getDouble("lng"))
                             ).title(lugar.get("nombre").toString())));

                 }

             }
         }




            