package com.pnpc.pavement;

import java.util.ArrayList;

/**
// * Created by jjshin on 3/10/16.
 */
public class Reading {

    public double start_lat;
    public double start_lon;
    public double end_lat;
    public double end_lon;
    public ArrayList<Float> acceleration_x;
    public ArrayList<Float> acceleration_y;
    public ArrayList<Float> acceleration_z;
    public int ride_id;
    public float start_time;
    public float end_time;
    public float angle_x;
    public float angle_y;
    public float angle_z;

    public Reading(){ }

    public double getStartLat() { return start_lat; }

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

    public void setAccelerationX(ArrayList<Float> acceleration_x) { this.acceleration_x = acceleration_x; }

    public ArrayList<Float> getAccelerationY() {
        return acceleration_y;
    }

    public void setAccelerationY(ArrayList<Float> acceleration_y) { this.acceleration_y = acceleration_y; }

    public ArrayList<Float> getAccelerationZ() {
        return acceleration_z;
    }

    public void setAccelerationZ(ArrayList<Float> acceleration_z) { this.acceleration_z = acceleration_z; }

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

    public void setAccelerations(ArrayList<Float> xArray, ArrayList<Float> yArray, ArrayList<Float> zArray){
        setAccelerationX(xArray);
        setAccelerationY(yArray);
        setAccelerationZ(zArray);
    }

    public void setAngles(float angleX, float angleY, float angleZ){
        setAngleX(angleX);
        setAngleY(angleY);
        setAngleZ(angleZ);
    }

}

