package com.pawelbryniarski.gpxviewer.tracksviewer.ui;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.pawelbryniarski.gpxviewer.tracksviewer.gpx.GPXParser;
import com.pawelbryniarski.gpxviewer.tracksviewer.repository.TracksReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPXViewerModel implements MapsMVP.Model {

    private static final String TAG = "GPXViewerModel";
    private final GPXParser parser;
    private final TracksReader tracksReader;


    public GPXViewerModel(GPXParser parser, TracksReader tracksReader) {
        this.parser = parser;
        this.tracksReader = tracksReader;
    }

    @Override
    public Map<String, List<LatLng>> getTracksData(List<String> selectedTracks) {
        Map<String, List<LatLng>> tracks = new HashMap<>();
        for (int i = 0; i < selectedTracks.size(); i++) {
            String newlySelectedTrack = selectedTracks.get(i);
            String path = "tracks/" + newlySelectedTrack + ".gpx";

            InputStream fis = tracksReader.getTrack(path);
            if (fis == null) {
                Log.e(TAG, "Unable to read " + path);
                continue;
            }
            List<LatLng> points = parser.parse(fis);
            tracks.put(newlySelectedTrack, points);
        }
        return tracks;
    }

    @Override
    public List<String> getAvailableTracks() {
        return tracksReader.getAvailableTracks();
    }
}
