package com.pawelbryniarski.gpxviewer.tracksviewer.ui;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

public class MapViewState {

    public final boolean trackPickerVisible;
    public final List<String> loadedTracks;
    public final Map<String, List<LatLng>> tracksData;
    public final boolean zoomPickerVisible;


    public MapViewState(boolean trackPickerVisible,
                        boolean zoomPickerVisible,
                        List<String> loadedTracks,
                        Map<String, List<LatLng>> tracksData) {
        this.trackPickerVisible = trackPickerVisible;
        this.loadedTracks = loadedTracks;
        this.tracksData = tracksData;
        this.zoomPickerVisible = zoomPickerVisible;
    }
}
