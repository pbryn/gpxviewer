package com.pawelbryniarski.gpxviewer.tracksviewer.gpx;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class GPXParser {

    public List<LatLng> parse(InputStream inputStream) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        List<LatLng> points = new ArrayList<>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            Node gpxNode = findInNodes("gpx", doc.getChildNodes());
            Node trkNode = findNext("trk", gpxNode);
            Node trksegNode = findNext("trkseg", trkNode);
            points.addAll(getAllPoints(trksegNode));
        } catch (Exception ex) {
            return Collections.emptyList();
        }
        return points;
    }

    private List<LatLng> getAllPoints(Node trksegNode) {
        List<LatLng> pointsList = new ArrayList<>();
        NodeList children = trksegNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeName().equals("trkpt")) {
                pointsList.add(extractGPSPoint(node));
            }
        }
        return pointsList;
    }

    private LatLng extractGPSPoint(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        float longitude = Float.valueOf(attributes.getNamedItem("lon").getNodeValue());
        float latitude = Float.valueOf(attributes.getNamedItem("lat").getNodeValue());
        return new LatLng(latitude, longitude);
    }

    private Node findNext(String nodeName, Node parent) {
        return findInNodes(nodeName, parent.getChildNodes());
    }

    private Node findInNodes(String nodeName, NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals(nodeName)) {
                return node;
            }
        }
        return null;
    }
}
