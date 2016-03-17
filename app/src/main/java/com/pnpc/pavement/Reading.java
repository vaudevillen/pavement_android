package com.pnpc.pavement;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
// * Created by jjshin on 3/10/16.
 */
public class Reading {

//    @SerializedName("start_lat")
    public double start_lat;
//    @SerializedName("start_lon")
    public double start_lon;
//    @SerializedName("end_lat")
    public double end_lat;
//    @SerializedName("end_lon")
    public double end_lon;
//    @SerializedName("acceleration_x")
    public ArrayList<Float> acceleration_x;
//    @SerializedName("acceleration_y")
    public ArrayList<Float> acceleration_y;
//    @SerializedName("acceleration_z")
    public ArrayList<Float> acceleration_z;
//    @SerializedName("ride_id")
    public int ride_id;
//    @SerializedName("start_time")
    public float start_time;
//    @SerializedName("end_time")
    public float end_time;
    public float angle_x;
    public float angle_y;
    public float angle_z;
//
//    public Reading(double start_lat, double start_lon, double end_lat, double end_lon, String acceleration_x, String acceleration_y,
//                String acceleration_z, int ride_id) {
//
//
//        this.start_lat = start_lat;
//        this.start_lon = start_lon;
//        this.end_lat = end_lat;
//        this.end_lon = end_lon;
//        this.acceleration_x = acceleration_x;
//        this.acceleration_y = acceleration_y;
//        this.acceleration_z = acceleration_z;
//        this.ride_id = ride_id;
//    }
    public Reading(){

    }

    public double getStartLat() {
        return start_lat;
    }

    public void setStartLat(double start_lat) {
        this.start_lat = start_lat;
    }

    public double getStartLon() {
        return start_lon;
    }

    public void setStartLon(double start_lon) {
        this.start_lon = start_lon;
    }

    public double getEndLat() {
        return end_lat;
    }

    public void setEndLat(double end_lat) {
        this.end_lat = end_lat;
    }

    public double getEndLon() {
        return end_lon;
    }

    public void setEndLon(double end_lon) {
        this.end_lon = end_lon;
    }

    public ArrayList<Float> getAccelerationX() {
        return acceleration_x;
    }

    public void setAccelerationX(ArrayList<Float> acceleration_x) {
        this.acceleration_x = acceleration_x;
    }

    public ArrayList<Float> getAccelerationY() {
        return acceleration_y;
    }

    public void setAccelerationY(ArrayList<Float> acceleration_y) {
        this.acceleration_y = acceleration_y;
    }

    public ArrayList<Float> getAccelerationZ() {
        return acceleration_z;
    }

    public void setAccelerationZ(ArrayList<Float> acceleration_z) {
        this.acceleration_z = acceleration_z;
    }

    public int getRideId() {
        return ride_id;
    }

    public void setRideId(int ride_id) {
        this.ride_id = ride_id;
    }

    public float getStartTime() {
        return start_time;
    }

    public void setStartTime(float start_time) {
        this.start_time = start_time;
    }

    public float getEndTime() { return end_time; }

    public void setEndTime(float end_time) {
        this.end_time = end_time;
    }

    public float getAngleX() { return angle_x; }

    public void setAngleX(float angle_x) { this.angle_x = angle_x; }

    public float getAngleY() { return angle_y; }

    public void setAngleY(float angle_y) { this.angle_y = angle_y; }

    public float getAngleZ() { return angle_z; }

    public void setAngleZ(float angle_z) { this.angle_z = angle_z; }

}

