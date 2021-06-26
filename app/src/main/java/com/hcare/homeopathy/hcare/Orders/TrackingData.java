package com.hcare.homeopathy.hcare.Orders;

import androidx.annotation.NonNull;

import java.util.List;

public class TrackingData {

    private List<ShipmentTrack> shipment_track;
    private List<ShipmentTrackActivity> shipment_track_activities;
    private String track_url;
    private int track_status;

    public void setShipment_track(List<ShipmentTrack> shipment_track) {
        this.shipment_track = shipment_track;
    }

    public void setTrack_url(String track_url) {
        this.track_url = track_url;
    }

    public int getTrack_status() {
        return track_status;
    }

    public void setTrack_status(int track_status) {
        this.track_status = track_status;
    }

    public List<ShipmentTrack> getShipment_track() {
        return shipment_track;
    }

    public List<ShipmentTrackActivity> getShipment_track_activities() {
        return shipment_track_activities;
    }

    public void setShipment_track_activities(List<ShipmentTrackActivity> shipment_track_activities) {
        this.shipment_track_activities = shipment_track_activities;
    }

    public String getTrack_url() {
        return track_url;
    }

    @NonNull
    @Override
    public String toString() {
        return "TrackingData{" +
                "shipment_track=" + shipment_track +
                ", shipment_track_activities=" + shipment_track_activities +
                ", track_url='" + track_url + '\'' +
                ", track_status=" + track_status +
                '}';
    }
}
