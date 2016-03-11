package com.akurian.shoppingcart;

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

        for (Category category : DataHolder.store) {
            if (set.contains(category.getName())) {
                cartMarkers.add(map.addMarker(new MarkerOptions()
                                .position(new LatLng(category.getLatitude(), category.getLongitude()))
                                .title(category.getName())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                ));
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
    }
}
