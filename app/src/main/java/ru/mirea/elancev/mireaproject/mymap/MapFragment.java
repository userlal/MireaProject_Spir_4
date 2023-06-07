package ru.mirea.elancev.mireaproject.mymap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import ru.mirea.elancev.mireaproject.databinding.FragmentMapBinding;

public class MapFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MapView mapView = null;
    private FragmentMapBinding binding;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private boolean isWork;
    private MyLocationNewOverlay locationNewOverlay;

    private String mParam1;
    private String mParam2;

    public MapFragment() {
    }
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Configuration.getInstance().load(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        int finePermissionStatus = ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermissionStatus = ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION);

        mapView = binding.mapview;
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        if (finePermissionStatus == PackageManager.PERMISSION_GRANTED &&
                coarsePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSION);
        }

        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(55.794229, 37.700772);
        mapController.setCenter(startPoint);

        locationNewOverlay = new MyLocationNewOverlay(new
                GpsMyLocationProvider(getContext().getApplicationContext()),mapView);
        locationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(this.locationNewOverlay);

        CompassOverlay compassOverlay = new CompassOverlay(requireContext(), new
                InternalCompassOrientationProvider(requireContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);


        final Context context = requireContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);


        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(55.794229, 37.700772));
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("РТУ МИРЭА на Стромынке")
                        .setMessage("Здесь находится всеми любимое и почитаемое место!\n" +
                                "Некогда являющееся Екатерининской богадельней!\n" +
                                "Стромынка, 20")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        mapView.getOverlays().add(marker);
        marker.setIcon(ResourcesCompat.getDrawable(getResources(),
                org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
        marker.setTitle("Home");

        Marker marker_2 = new Marker(mapView);
        marker_2.setPosition(new GeoPoint(55.7520, 37.6175));
        marker_2.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Kremlin")
                        .setMessage("Здесь находится \"сердце\" Москвы.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        mapView.getOverlays().add(marker_2);
        marker_2.setIcon(ResourcesCompat.getDrawable(getResources(),
                org.osmdroid.library.R.drawable.zoom_in, null));
        marker_2.setTitle("Kremlin");


        Marker marker_3 = new Marker(mapView);
        marker_3.setPosition(new GeoPoint(55.6883, 37.8093));
        marker_3.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Дед мороз ")
                        .setMessage("Есть у Московского Деда Мороза волшебные друзья — Шуршики.\n" +
                                "Маленьких лесных озорников нашел как-то Дедушка, гуляя по лесу.\n" +
                                "Стоит прислушаться, и ты услышишь «шур-шур» — это Шуршики\n" +
                                "веселяться в Усадьбе! Весной Дед Мороз уезжает в холодные\n" +
                                "зимние края, а Шуршики остаются за главных в его Московской\n" +
                                "Усадьбе! Приходите в парк летом и познакомьтесь с неугамонными\n" +
                                "\t\tШуршиками!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        mapView.getOverlays().add(marker_3);
        marker_3.setIcon(ResourcesCompat.getDrawable(getResources(),
                org.osmdroid.library.R.drawable.center, null));
        marker_3.setTitle("Ded Cold!");

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));
    }
    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));

    }
}