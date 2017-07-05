package com.pawelbryniarski.gpxviewer.tracksviewer.ui;

import com.google.android.gms.maps.model.LatLng;
import com.pawelbryniarski.gpxviewer.tracksviewer.gpx.GPXParser;
import com.pawelbryniarski.gpxviewer.tracksviewer.repository.AssetTracksReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsPresenter implements MapsMVP.Presenter {

    private MapViewState state;
    private MapsActivity activity;
    private MapsMVP.Model model;

    @Override
    public void attach(final MapsActivity activity, MapViewState initialState) {
        state = initialState;
        this.activity = activity;
        model = new GPXViewerModel(new GPXParser(), new AssetTracksReader(activity));
        initialize(state);
    }

    private void initialize(MapViewState initialState) {
        if (!initialState.loadedTracks.isEmpty()) {
            state = new MapViewState(state.trackPickerVisible,
                                     state.zoomPickerVisible,
                                     state.loadedTracks,
                                     model.getTracksData(initialState.loadedTracks));
            activity.draw(state.tracksData);
        }
    }

    @Override
    public MapViewState detach() {
        this.activity = null;
        return state;
    }

    @Override
    public void onZoomRequest() {
        activity.showZoomPicker(state.loadedTracks.toArray(new String[state.loadedTracks.size()]));
    }

    @Override
    public void onZoomPicked(String trackName) {
        activity.zoom(state.tracksData.get(trackName).get(0));
    }

    @Override
    public void onLoadTracksRequest() {

        List<String> allTracks = model.getAvailableTracks();
        boolean[] selected = new boolean[allTracks.size()];
        for (int i = 0; i < allTracks.size(); i++) {
            if (state.loadedTracks.contains(allTracks.get(i))) {
                selected[i] = true;
            }
        }
        activity.showTracksPicker(selected, allTracks.toArray(new String[allTracks.size()]));
    }

    @Override
    public void onTracksPicked(boolean[] selectedTracks, String[] tracksNames) {
        List<String> newlySelectedTracks = new ArrayList<>();

        for (int i = 0; i < selectedTracks.length; i++) {
            if (selectedTracks[i]) {
                newlySelectedTracks.add(tracksNames[i]);
            }
        }

        Map<String, List<LatLng>> tracks = model.getTracksData(newlySelectedTracks);

        state = new MapViewState(false, false, newlySelectedTracks, tracks);
        activity.draw(tracks);
    }
}
