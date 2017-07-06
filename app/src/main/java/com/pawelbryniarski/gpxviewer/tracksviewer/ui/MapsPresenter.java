package com.pawelbryniarski.gpxviewer.tracksviewer.ui;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class MapsPresenter implements MapsMVP.Presenter {

    private final MapsMVP.Model model;

    private MapViewState state;
    private MapsMVP.View view;

    @Inject public MapsPresenter(MapsMVP.Model model) {
        this.model = model;
    }


    @Override
    public void attach(final MapsMVP.View activity, MapViewState initialState) {
        state = initialState;
        this.view = activity;
        initialize(state);
    }

    private void initialize(MapViewState initialState) {
        if (!initialState.loadedTracks.isEmpty()) {
            state = new MapViewState(state.trackPickerVisible,
                    state.zoomPickerVisible,
                    state.loadedTracks,
                    model.getTracksData(initialState.loadedTracks));
            view.draw(state.tracksData);
        }

        if (initialState.zoomPickerVisible) {
            onZoomRequest();
        }

        if (initialState.trackPickerVisible) {
            onLoadTracksRequest();
        }
    }

    @Override
    public MapViewState detach() {
        this.view = null;
        return state;
    }

    @Override
    public void onZoomRequest() {
        view.showZoomPicker(state.loadedTracks.toArray(new String[state.loadedTracks.size()]));
        state = new MapViewState(state.trackPickerVisible, true, state.loadedTracks, state.tracksData);
    }

    @Override
    public void onZoomPicked(String trackName) {
        view.zoom(state.tracksData.get(trackName).get(0));
        state = new MapViewState(state.trackPickerVisible, false, state.loadedTracks, state.tracksData);
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
        state = new MapViewState(true, state.zoomPickerVisible, state.loadedTracks, state.tracksData);
        view.showTracksPicker(selected, allTracks.toArray(new String[allTracks.size()]));
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

        view.draw(tracks);
        if (!tracks.isEmpty()) {
            view.showZoomPicker(tracksNames);
            state = new MapViewState(false, true, newlySelectedTracks, tracks);
        } else {
            state = new MapViewState(false, false, newlySelectedTracks, tracks);
        }
    }
}
