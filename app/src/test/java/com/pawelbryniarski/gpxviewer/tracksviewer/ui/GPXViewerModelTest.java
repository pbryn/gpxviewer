package com.pawelbryniarski.gpxviewer.tracksviewer.ui;

import com.google.android.gms.maps.model.LatLng;
import com.pawelbryniarski.gpxviewer.tracksviewer.gpx.GPXParser;
import com.pawelbryniarski.gpxviewer.tracksviewer.repository.TracksReader;

import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by pawelbryniarski on 06.07.2017.
 */
public class GPXViewerModelTest {

    GPXParser gpxParserMock = mock(GPXParser.class);
    TracksReader tracksReader = mock(TracksReader.class);

    GPXViewerModel model = new GPXViewerModel(gpxParserMock, tracksReader);

    @Test
    public void returnsAvailableTracks() throws Exception {
        List<String> tracks = Arrays.asList("track1", "track2");
        when(tracksReader.getAvailableTracks()).thenReturn(tracks);

        List<String> actual = model.getAvailableTracks();

        assertEquals(tracks, actual);
    }


    @Test
    public void returnsTrackData() throws Exception {
        List<String> trackNames = Arrays.asList("track1", "track2");

        List<LatLng> firstTrack = Arrays.asList(new LatLng(1.0, 1.0), new LatLng(1.0, 1.0));
        List<LatLng> secondTrack = Arrays.asList(new LatLng(2.0, 2.0), new LatLng(2.0, 2.0));

        Map<String, List<LatLng>> expected = new HashMap<>();
        expected.put("track1", firstTrack);
        expected.put("track2", secondTrack);

        when(tracksReader.getTrack("tracks/track1.gpx")).thenReturn(mock(InputStream.class));
        when(tracksReader.getTrack("tracks/track2.gpx")).thenReturn(mock(InputStream.class));

        when(gpxParserMock.parse(any(InputStream.class)))
                .thenReturn(firstTrack)
                .thenReturn(secondTrack);

        Map<String, List<LatLng>> actual = model.getTracksData(trackNames);


        assertEquals(expected, actual);
    }
}