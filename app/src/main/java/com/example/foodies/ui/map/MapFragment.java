package com.example.foodies.ui.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.foodies.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {
    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.text_map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(37.4219999,-122.0862462))
                        .zoom(10)
                        .bearing(0)
                        .tilt(45)
                        .build();

//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

//                mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(37.4629101,-122.2449094))
//                        .title("Iron Man")
//                        .snippet("His Talent : Plenty of money"));
//
//                mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(37.3092293,-122.1136845))
//                        .title("Captain America"));
            }
        });


        return rootView;
    }

//    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
//        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
//        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
//        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        vectorDrawable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//    }
}

//public class MapFragment extends Fragment {
//
//    private MapViewModel mapViewModel;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        mapViewModel =
//                ViewModelProviders.of(this).get(MapViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_map, container, false);
//        final TextView textView = root.findViewById(R.id.text_map);
//        mapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }
//}

