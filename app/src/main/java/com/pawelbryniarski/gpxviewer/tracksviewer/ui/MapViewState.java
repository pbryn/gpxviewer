package com.pawelbryniarski.gpxviewer.tracksviewer.ui;

public class MapViewState {

    public final boolean trackPickerVisible;
    public final int[] loadedTracksIds;
//    public final int[] loadedTracksIds;


    public MapViewState(boolean trackPickerVisible, int[] loadedTracksIds) {
        this.trackPickerVisible = trackPickerVisible;
        this.loadedTracksIds = loadedTracksIds;
    }
}
