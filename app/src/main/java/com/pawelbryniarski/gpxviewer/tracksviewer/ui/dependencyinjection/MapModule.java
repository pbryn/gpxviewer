package com.pawelbryniarski.gpxviewer.tracksviewer.ui.dependencyinjection;

import android.content.Context;

import com.pawelbryniarski.gpxviewer.tracksviewer.gpx.GPXParser;
import com.pawelbryniarski.gpxviewer.tracksviewer.repository.AssetTracksReader;
import com.pawelbryniarski.gpxviewer.tracksviewer.ui.GPXViewerModel;
import com.pawelbryniarski.gpxviewer.tracksviewer.ui.MapsMVP;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pawelbryniarski on 06.07.2017.
 */

@Module
public class MapModule {

    private final Context context;

    public MapModule(Context context) {
        this.context = context;
    }

    @Provides
    Context context(){
        return  context;
    }

    @Provides
    MapsMVP.Model model(GPXParser parser){
        return new GPXViewerModel(parser, new AssetTracksReader(context));
    }

}
