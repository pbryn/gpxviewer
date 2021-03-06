package com.pawelbryniarski.gpxviewer.tracksviewer.ui;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MapsPresenter implements MapsMVP.Presenter {

    private final Scheduler ioScheduler;
    private final Scheduler uiScheduler;
    private final MapsMVP.Model model;

    private MapViewState state;
    private MapsMVP.View view;
    private CompositeDisposable compositeDisposable;

    public MapsPresenter(Scheduler ioScheduler, Scheduler uiScheduler, MapsMVP.Model model) {
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
        this.model = model;
        this.compositeDisposable = new CompositeDisposable();
    }


    @Override
    public void attach(final MapsMVP.View activity, MapViewState initialState) {
        this.view = activity;
        initialize(initialState);
    }

    private void initialize(final MapViewState initialState) {
        if (!initialState.loadedTracks().isEmpty()) {
            compositeDisposable.add(
            Observable.just(initialState)
                    .subscribeOn(ioScheduler)
                    .map(new Function<MapViewState, MapViewState>() {
                        @Override
                        public MapViewState apply(@NonNull MapViewState mapViewState) throws Exception {
                            return initialState.changeState()
                                    .withTracksData(model.getTracksData(initialState.loadedTracks()))
                                    .apply();
                        }
                    })
                    .observeOn(uiScheduler)
                    .subscribe(new Consumer<MapViewState>() {
                        @Override
                        public void accept(@NonNull MapViewState mapViewState) throws Exception {
                            state = initialState.changeState()
                                    .withTracksData(model.getTracksData(initialState.loadedTracks()))
                                    .apply();
                            showInitialPickers(initialState);
                            view.draw(state.tracksData());
                        }
                    }));
        } else {
            state = initialState;
            showInitialPickers(initialState);
        }
    }

    private void showInitialPickers(MapViewState initialState) {
        if (initialState.zoomPickerVisible()) {
            onZoomRequest();
        }

        if (initialState.trackPickerVisible()) {
            onLoadTracksRequest();
        }
    }

    @Override
    public MapViewState detach() {
        this.view = null;
        compositeDisposable.dispose();
        return state;
    }

    @Override
    public void onZoomRequest() {
        view.showZoomPicker(state.loadedTracks().toArray(new String[state.loadedTracks().size()]));
        state = state.changeState()
                .withZoomPickerVisible(true)
                .apply();
    }

    @Override
    public void onZoomPicked(String trackName) {
        view.zoom(state.tracksData().get(trackName).get(0));
        state = state.changeState()
                .withZoomPickerVisible(false)
                .apply();
    }

    @Override
    public void onLoadTracksRequest() {
        List<String> allTracks = model.getAvailableTracks();
        boolean[] selected = new boolean[allTracks.size()];
        for (int i = 0; i < allTracks.size(); i++) {
            if (state.loadedTracks().contains(allTracks.get(i))) {
                selected[i] = true;
            }
        }
        state = state.changeState()
                .withTracksPickerVisible(true)
                .apply();
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
        MapViewState.StateChanger stateChanger = state.changeState()
                .withTracksPickerVisible(false)
                .withLoadedTracks(newlySelectedTracks);
        if (tracks.isEmpty()) {
            state = stateChanger.withZoomPickerVisible(false).apply();

        } else if (tracks.size() == 1) {
            state = stateChanger.withZoomPickerVisible(false)
                    .withTracksData(tracks)
                    .apply();
            view.zoom(tracks.get(newlySelectedTracks.get(0)).get(0));


        } else {
            state = stateChanger.withZoomPickerVisible(true)
                    .withTracksData(tracks)
                    .apply();
            view.showZoomPicker(newlySelectedTracks.toArray(new String[newlySelectedTracks.size()]));
        }
    }
}
