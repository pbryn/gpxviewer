package com.pawelbryniarski.gpxviewer.tracksviewer.gpx;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GPXParserTest {

    @Test
    public void parsesGPXFromInputStream() throws Exception {
        GPXParser parser = new GPXParser();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("Track1.gpx");

        List<LatLng> readPoints = parser.parse(in);

        List<LatLng> expected = new ArrayList<>();
        expected.add(new LatLng(52.376970f, 4.895370f));
        expected.add(new LatLng(52.377060f, 4.895180f));

        assertEquals(expected, readPoints);
    }
}