package com.pawelbryniarski.gpxviewer.tracksviewer.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

final public class AssetTracksReader implements TracksReader {

    private final Context context;

    public AssetTracksReader(Context context) {
        this.context = context;
    }

    @Override
    public List<String> getAvailableTracks() {
        String[] trackFiles;

        try {
            trackFiles = context.getAssets().list("tracks");
        } catch (IOException e) {
            throw new RuntimeException("Unable to read tracks from assets");
        }
        List<String> names = new ArrayList<>(trackFiles.length);
        for (String trackFileName : trackFiles) {
            names.add(removeFileExtension(trackFileName));
        }
        return names;
    }

    @Override
    public InputStream getTrack(String path) {
        try {
            return context.getAssets().open(path);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read " + path + "from assets" + ". This should not happen");
        }
    }

    @NonNull
    private String removeFileExtension(String trackFileName) {
        return trackFileName.substring(0, trackFileName.lastIndexOf(".gpx"));
    }
}
