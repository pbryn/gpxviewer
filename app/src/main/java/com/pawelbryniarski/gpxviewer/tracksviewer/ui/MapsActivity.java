package com.pawelbryniarski.gpxviewer.tracksviewer.ui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.animation.OvershootInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pawelbryniarski.gpxviewer.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsMVP.View {

    public static final String ZOOM_PICKER_VISIBLE_KEY = "ZOOM_PICKER_VISIBLE";
    public static final String LOADED_TRACKS_KEY = "LOADED_TRACKS";
    private static final String TRACKS_PICKER_VISIBLE_KEY = "TRACKS_PICKER_VISIBLE";
    public static final int ZOOM_RATIO = 10;
    public static final int MAP_LINES_WIDTH = 5;
    private static final int[] colors = new int[]{Color.BLUE, Color.RED, Color.BLACK, Color.YELLOW,
            Color.WHITE};
    private GoogleMap mMap;
    private MapsPresenter mapsPresenter = new MapsPresenter();
    private Bundle savedState;

    @OnClick(R.id.select_tracks)
    public void showSelectTracksDialog() {
        mapsPresenter.onLoadTracksRequest();
    }

    @OnClick(R.id.zoom_to_track)
    public void showZoomTrackDialog() {
        mapsPresenter.onZoomRequest();
    }

    @BindView(R.id.select_tracks)
    FloatingActionButton selectTracksButton;

    @BindView(R.id.zoom_to_track)
    FloatingActionButton zoomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        savedState = savedInstanceState;

        zoomButton.setScaleX(0);
        zoomButton.setScaleY(0);
        selectTracksButton.setScaleX(0);
        selectTracksButton.setScaleY(0);
        selectTracksButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectTracksButton.animate().setInterpolator(new OvershootInterpolator()).scaleX(1).scaleY(1).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        zoomButton.animate().setInterpolator(new OvershootInterpolator()).scaleX(1).scaleY(1).start();
                    }
                }).start();

            }
        }, 500);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapViewState initialState;
        if (savedState != null) {
            initialState = new MapViewState(savedState.getBoolean(TRACKS_PICKER_VISIBLE_KEY),
                    savedState.getBoolean(ZOOM_PICKER_VISIBLE_KEY),
                    Arrays.asList(savedState.getStringArray(LOADED_TRACKS_KEY)),
                    null);
        } else {
            initialState = new MapViewState(
                    false,
                    false,
                    new ArrayList<String>(),
                    new HashMap<String, List<LatLng>>());
        }
        savedState = null;
        mapsPresenter.attach(this, initialState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        MapViewState stateToSave = mapsPresenter.detach();
        outState.putBoolean(TRACKS_PICKER_VISIBLE_KEY, stateToSave.trackPickerVisible);
        outState.putBoolean(ZOOM_PICKER_VISIBLE_KEY, stateToSave.zoomPickerVisible);
        outState.putStringArray(LOADED_TRACKS_KEY,
                stateToSave.loadedTracks.toArray(new String[stateToSave.loadedTracks.size()]));
        // do not save actual tracks as this may be too much data
        super.onSaveInstanceState(outState);
    }

    @Override
    public void draw(Map<String, List<LatLng>> tracks) {
        mMap.clear();
        int colorIndex = 0;
        for (Map.Entry<String, List<LatLng>> trackData : tracks.entrySet()) {
            mMap.addPolyline(new PolylineOptions()
                    .addAll(trackData.getValue())
                    .width(MAP_LINES_WIDTH)
                    .color(colors[colorIndex % colors.length])
                    .geodesic(true));
            mMap.addMarker(new MarkerOptions().title(trackData.getKey())
                    .position(trackData.getValue().get(0)));
            colorIndex++;
        }
    }

    @Override
    public void zoom(LatLng zoomPoint) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoomPoint, ZOOM_RATIO));
    }

    @Override

    public void showZoomPicker(final String[] trackNames) {
        new AlertDialog
                .Builder(this)
                .setTitle(R.string.pick_track_to_zoom)
                .setSingleChoiceItems(trackNames, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mapsPresenter.onZoomPicked(trackNames[which]);
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void showTracksPicker(final boolean[] selectedTracks, final String[] tracksNames) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.pick_tracks_title)
                .setMultiChoiceItems(tracksNames, selectedTracks,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                selectedTracks[which] = isChecked;
                            }
                        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mapsPresenter.onTracksPicked(selectedTracks, tracksNames);
                    }
                })
                .create()
                .show();
    }
}