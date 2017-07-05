package com.pawelbryniarski.gpxviewer.tracksviewer.repository;

import java.io.InputStream;
import java.util.List;

public interface TracksReader {
    List<String> getAvailableTracks();
    InputStream getTrack(String path);
}
