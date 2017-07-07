package com.pawelbryniarski.gpxviewer.tracksviewer.ui;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by pawelbryniarski on 07.07.2017.
 */
public class MapsPresenterTest {

    MapsMVP.Model modelMock = mock(MapsMVP.Model.class);
    MapsMVP.View viewMock = mock(MapsMVP.View.class);
    MapsPresenter tested = new MapsPresenter(Schedulers.trampoline(), Schedulers.trampoline(), modelMock);
    MapViewState stateMock = mock(MapViewState.class);
    MapViewState stateMockChanged = mock(MapViewState.class);
    MapViewState.StateChanger stateChangeMock = mock(MapViewState.StateChanger.class);

    @Before
    public void setUp() throws Exception {
        when(stateMock.changeState()).thenReturn(stateChangeMock);
        when(stateChangeMock.withLoadedTracks(ArgumentMatchers.<String>anyList())).thenReturn(stateChangeMock);
        when(stateChangeMock.withTracksData(ArgumentMatchers.<String, List<LatLng>>anyMap())).thenReturn(stateChangeMock);
        when(stateChangeMock.withZoomPickerVisible(anyBoolean())).thenReturn(stateChangeMock);
        when(stateChangeMock.withTracksPickerVisible(anyBoolean())).thenReturn(stateChangeMock);
        when(stateChangeMock.withTracksPickerVisible(anyBoolean())).thenReturn(stateChangeMock);
        when(stateChangeMock.apply()).thenReturn(stateMockChanged);
    }

    @Test
    public void drawsTracksOnView_WhenInitialStateContainsLoadedTracks() throws Exception {
        List<String> tracksList = new ArrayList<>();
        tracksList.add("track");
        Map<String, List<LatLng>> tracksData = Collections.emptyMap();
        when(modelMock.getTracksData(tracksList)).thenReturn(tracksData);
        when(stateMock.loadedTracks()).thenReturn(tracksList);

        tested.attach(viewMock, stateMock);

        verify(viewMock).draw(tracksData);
    }

    @Test
    public void doesntDrawTracksOnView_WhenInitialStateContainsLoadedTracks() throws Exception {
        List<String> tracksList = Collections.emptyList();
        Map<String, List<LatLng>> tracksData = Collections.emptyMap();
        when(modelMock.getTracksData(tracksList)).thenReturn(tracksData);
        when(stateMock.loadedTracks()).thenReturn(tracksList);

        tested.attach(viewMock, stateMock);

        verify(viewMock, times(0)).draw(ArgumentMatchers.<String, List<LatLng>>anyMap());
    }

    @Test
    public void showsZoomPicker_WhenZoomPickerIsVisibleInInitialState() throws Exception {
        when(stateMock.loadedTracks()).thenReturn(Collections.<String>emptyList());
        when(stateMock.zoomPickerVisible()).thenReturn(true);

        tested.attach(viewMock, stateMock);

        verify(viewMock).showZoomPicker(any(String[].class));
    }

    @Test
    public void doesntShowZoomPicker_WhenZoomPickerIsNotVisibleInInitialState() throws Exception {
        when(stateMock.loadedTracks()).thenReturn(Collections.<String>emptyList());
        when(stateMock.zoomPickerVisible()).thenReturn(false);

        tested.attach(viewMock, stateMock);

        verify(viewMock, times(0)).showZoomPicker(any(String[].class));
    }

    @Test
    public void showsTracksPicker_WhenTracksPickerIsVisibleInInitialState() throws Exception {
        when(stateMock.loadedTracks()).thenReturn(Collections.<String>emptyList());
        when(stateMock.trackPickerVisible()).thenReturn(true);

        tested.attach(viewMock, stateMock);

        verify(viewMock).showTracksPicker(any(boolean[].class), any(String[].class));
    }

    @Test
    public void doesntShowTracksPicker_WhenTracksPickerIsNotVisibleInInitialState() throws Exception {
        when(stateMock.loadedTracks()).thenReturn(Collections.<String>emptyList());
        when(stateMock.trackPickerVisible()).thenReturn(false);

        tested.attach(viewMock, stateMock);

        verify(viewMock, times(0)).showTracksPicker(any(boolean[].class), any(String[].class));
    }

    @Test
    public void showsZoomPicker() throws Exception {
        tested.attach(viewMock, stateMock);

        tested.onZoomRequest();

        verify(viewMock).showZoomPicker(any(String[].class));
    }

    @Test
    public void showsTrackPicker() throws Exception {
        tested.attach(viewMock, stateMock);

        tested.onLoadTracksRequest();

        verify(viewMock).showTracksPicker(any(boolean[].class), any(String[].class));
    }

    @Test
    public void zoomsToSelectedTrack() throws Exception {
        Map<String, List<LatLng>> tracksData = new HashMap<>();
        List<LatLng> track = Collections.singletonList(new LatLng(1, 2));
        tracksData.put("track", track);
        when(stateMock.tracksData()).thenReturn(tracksData);
        tested.attach(viewMock, stateMock);

        tested.onZoomPicked("track");

        verify(viewMock).zoom(new LatLng(1, 2));
    }

    @Test
    public void drawsTracksOnView() throws Exception {
        //TODO
    }
}