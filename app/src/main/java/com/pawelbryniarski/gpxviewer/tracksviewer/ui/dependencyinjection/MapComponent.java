package com.pawelbryniarski.gpxviewer.tracksviewer.ui.dependencyinjection;

import com.pawelbryniarski.gpxviewer.tracksviewer.ui.MapsActivity;

import dagger.Component;

/**
 * Created by pawelbryniarski on 06.07.2017.
 */

@Component(modules = {MapModule.class})
public interface MapComponent {
    void inject(MapsActivity mapsActivity);
}
