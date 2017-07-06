package com.pawelbryniarski.gpxviewer.tracksviewer.ui;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

public interface MapsMVP {

    interface View{
        void draw(Map<String, List<LatLng>> tracks);
        void zoom(LatLng zoomPoint);
        void showZoomPicker(final String[] trackNames);
        void showTracksPicker(final boolean[] selectedTracks, final String[] tracksNames);
    }

    interface Presenter{
        void attach(final MapsMVP.View view, MapViewState initialState);
        MapViewState detach();
        void onTracksPicked(boolean[] selectedTracks, String[] tracksNames);
        void onLoadTracksRequest();
        void onZoomPicked(String trackName);
        void onZoomRequest();
    }

    interface Model{
        Map<String, List<LatLng>> getTracksData(List<String> selectedTracks);
        List<String> getAvailableTracks();
    }
}
