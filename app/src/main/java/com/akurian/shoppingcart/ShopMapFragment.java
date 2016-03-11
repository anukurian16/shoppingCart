package com.akurian.shoppingcart;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akurian.shoppingcart.models.Category;
import com.akurian.shoppingcart.models.DataHolder;
import com.akurian.shoppingcart.models.Product;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ShopMapFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container,
                false);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        map = mMapView.getMap();

        updateUI();

        return v;
    }


    private void updateUI() {

        Set<String> set = new HashSet<>();
        for (Product product : DataHolder.cart) {
            set.add(product.getCategory());
        }

        List<Marker> cartMarkers = new ArrayList<>();
        List<Marker> nonCartMarkers = new ArrayList<>();
        List<Category> pathData = new ArrayList<>();

        for (Category category : DataHolder.store) {
            if (set.contains(category.getName())) {
                cartMarkers.add(map.addMarker(new MarkerOptions()
                                .position(new LatLng(category.getLatitude(), category.getLongitude()))
                                .title(category.getName())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                ));

                pathData.add(category);

            } else {
                nonCartMarkers.add(map.addMarker(new MarkerOptions()
                                .position(new LatLng(category.getLatitude(), category.getLongitude()))
                                .title(category.getName())

                ));
            }
        }

        Marker startMarker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(DataHolder.startLat, DataHolder.startLong))
                        .title("Entrance")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        );

        List<Marker> allMarkers = new ArrayList<>();
        allMarkers.addAll(cartMarkers);
        allMarkers.addAll(nonCartMarkers);
        allMarkers.add(startMarker);

        System.out.println("Size : " + allMarkers.size());

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : allMarkers) {
            builder.include(marker.getPosition());
        }
        final LatLngBounds bounds = builder.build();


        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
            }
        });

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // Move camera.
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                // Remove listener to prevent position reset on camera move.
                map.setOnCameraChangeListener(null);
            }
        });

        Category entrance = new Category();
        entrance.setName("Entrance");
        entrance.setLatitude(DataHolder.startLat);
        entrance.setLongitude(DataHolder.startLong);
        pathData.add(entrance);

        MST(pathData);
    }

    private void MST(List<Category> data) {

        int N = data.size();
        double[][] adj = new double[N][N];

        final double INF = Double.MAX_VALUE;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) adj[i][j] = INF;
                else {
                    Category catA = data.get(i);
                    Category catB = data.get(j);

                    float[] result = new float[1];
                    Location.distanceBetween(catA.getLatitude(), catA.getLongitude(),
                            catB.getLatitude(), catB.getLongitude(), result);

                    adj[i][j] = result[0];
                }
            }
        }


        boolean[] visited = new boolean[N];
        visited[N - 1] = true;

        int count = 0;
        int NIL = -1;
        while (count < N - 1) {
            double min = INF;
            int u = NIL, v = NIL;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (adj[i][j] < min && visited[i]) {
                        min = adj[i][j];
                        u = i;
                        v = j;
                    }
                }
            }

            if (visited[u] == false || visited[v] == false) {

                System.out.printf("%d - %d\n", u, v);

                PolylineOptions polyLineOptions = new PolylineOptions();

                Category catA = data.get(u);
                polyLineOptions.add(new LatLng(catA.getLatitude(), catA.getLongitude()));

                Category catB = data.get(v);
                polyLineOptions.add(new LatLng(catB.getLatitude(), catB.getLongitude()));

                polyLineOptions.width(5);
                polyLineOptions.color(Color.BLUE);
                map.addPolyline(polyLineOptions);

                visited[v] = true;

                count++;

            }
            adj[u][v] = INF;
            adj[v][u] = INF;
        }


    }
}
