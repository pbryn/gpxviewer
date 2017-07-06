package com.pawelbryniarski.gpxviewer.tracksviewer.ui;

import com.google.android.gms.maps.model.LatLng;
import com.pawelbryniarski.gpxviewer.tracksviewer.gpx.GPXParser;
import com.pawelbryniarski.gpxviewer.tracksviewer.repository.TracksReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPXViewerModel implements MapsMVP.Model {

    private final GPXParser parser;
    private final TracksReader tracksReader;

    public GPXViewerModel(GPXParser parser, TracksReader tracksReader) {
        this.parser = parser;
        this.tracksReader = tracksReader;
    }

    @Override
    public Map<String, List<LatLng>> getTracksData(List<String> selectedTracks) {
        Map<String, List<LatLng>> tracks = new HashMap<>();

        for (String selectedTrack : selectedTracks) {
            String path = "tracks/" + selectedTrack + ".gpx";

            InputStream fis = tracksReader.getTrack(path);

            List<LatLng> points = parser.parse(fis);
            tracks.put(selectedTrack, points);
        }
        return tracks;
    }

    @Override
    public List<String> getAvailableTracks() {
        return tracksReader.getAvailableTracks();
    }
}
