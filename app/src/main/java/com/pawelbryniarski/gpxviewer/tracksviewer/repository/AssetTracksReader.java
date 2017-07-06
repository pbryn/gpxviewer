package com.pawelbryniarski.gpxviewer.tracksviewer.repository;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AssetTracksReader implements TracksReader {

    private final Context context;

    public AssetTracksReader(Context context) {
        this.context = context;
    }

    @Override
    public List<String> getAvailableTracks() {
        String[] fullNamesWithExtension;
        try {
            fullNamesWithExtension = context.getAssets().list("tracks");
        } catch (IOException e) {
            throw new RuntimeException("Unable to read tracks from assets");
        }
        List<String> names = new ArrayList<>(fullNamesWithExtension.length);
        for (String aFullNamesWithExtension : fullNamesWithExtension) {
            names.add(aFullNamesWithExtension.substring(0, aFullNamesWithExtension.lastIndexOf(".gpx")));
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
}
