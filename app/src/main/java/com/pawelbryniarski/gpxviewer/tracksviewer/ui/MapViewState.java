package com.pawelbryniarski.gpxviewer.tracksviewer.ui;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MapViewState {

    public final boolean trackPickerVisible;
    public final List<String> loadedTracks;
    public final Map<String, List<LatLng>> tracksData;
    public final boolean zoomPickerVisible;


    private MapViewState(boolean trackPickerVisible,
                         boolean zoomPickerVisible,
                         List<String> loadedTracks,
                         Map<String, List<LatLng>> tracksData) {
        this.trackPickerVisible = trackPickerVisible;
        this.loadedTracks = loadedTracks;
        this.tracksData = tracksData;
        this.zoomPickerVisible = zoomPickerVisible;
    }

    public static MapViewState initialState() {
        return new MapViewState(false, false, Collections.<String>emptyList(), Collections.<String, List<LatLng>>emptyMap());
    }

    public StateChanger changeState() {
        return new StateChanger(this);
    }

    public class StateChanger {

        private final MapViewState initialState;

        private Boolean tracksPickerVisible;
        private Boolean zoomPickerVisible;
        private List<String> loadedTracks;
        private Map<String, List<LatLng>> tracksData;

        private StateChanger(MapViewState initialState) {
            this.initialState = initialState;
        }

        public StateChanger withZoomPickerVisibile(boolean visible) {
            this.zoomPickerVisible = visible;
            return this;
        }

        public StateChanger withTracksPickerVisible(boolean visible) {
            this.tracksPickerVisible = visible;
            return this;
        }

        public StateChanger withLoadedTracks(List<String> loadedTracks) {
            this.loadedTracks = loadedTracks;
            return this;
        }

        public StateChanger withTracksData(Map<String, List<LatLng>> tracksData) {
            this.tracksData = tracksData;
            return this;
        }

        public MapViewState apply() {
            boolean newTracksPickerVisible =
                    tracksPickerVisible != null ? tracksPickerVisible : initialState.trackPickerVisible;
            boolean newZoomPickerVisible =
                    zoomPickerVisible != null ? zoomPickerVisible : initialState.zoomPickerVisible;
            List<String> newLoadedTracks = loadedTracks != null ? loadedTracks : initialState.loadedTracks;
            Map<String, List<LatLng>> newTracksData = tracksData != null ? tracksData : initialState.tracksData;
            return new MapViewState(newTracksPickerVisible, newZoomPickerVisible, newLoadedTracks, newTracksData);
        }
    }
}
