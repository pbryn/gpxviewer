package com.pawelbryniarski.gpxviewer.tracksviewer.repository;

import android.content.Context;
import android.content.res.AssetManager;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by pawelbryniarski on 06.07.2017.
 */
public class AssetTracksReaderTest {
    Context contextMock = mock(Context.class);
    AssetManager assetManagerMock = mock(AssetManager.class);

    AssetTracksReader assetTracksReader = new AssetTracksReader(contextMock);

    @Before
    public void setUp() throws Exception {
        when(contextMock.getAssets()).thenReturn(assetManagerMock);
    }

    @Test
    public void returnsTrackNames() throws Exception {
        String[] trackNames = new String[]{"track1.gpx", "track2.gpx"};
        when(assetManagerMock.list("tracks")).thenReturn(trackNames);

        List<String> actual = assetTracksReader.getAvailableTracks();

        assertEquals("Number of available tracks should be 2", 2, actual.size());
        assertEquals("First track name is correct", actual.get(0), "track1");
        assertEquals("Second track name is correct", actual.get(1), "track2");
    }


    @Test(expected = RuntimeException.class)
    public void throwsException_WhenReadingTracksListFails() throws Exception {
        when(assetManagerMock.list("tracks")).thenThrow(new IOException());

        assetTracksReader.getAvailableTracks();
    }

    @Test
    public void returnsStreamToTrack() throws Exception {
        InputStream inputStreamMock = mock(InputStream.class);
        when(assetManagerMock.open("path")).thenReturn(inputStreamMock);

        InputStream actual = assetTracksReader.getTrack("path");

        assertEquals(inputStreamMock, actual);
    }

    @Test(expected = RuntimeException.class)
    public void throwsException_WhenOpeningTrackFileFails() throws Exception {
        when(assetManagerMock.open("path")).thenThrow(new IOException());

        assetTracksReader.getTrack("path");
    }
}