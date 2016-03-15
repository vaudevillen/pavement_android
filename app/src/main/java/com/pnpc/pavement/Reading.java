package com.pnpc.pavement;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
// * Created by jjshin on 3/10/16.
 */
public class Reading {

    @SerializedName("reading")
    public HashMap reading;

    @SerializedName("start_lat")
    public double start_lat;

    @SerializedName("start_lon")
    public double start_lon;

    @SerializedName("end_lat")
    public double end_lat;

    @SerializedName("end_lon")
    public double end_lon;

    @SerializedName("acceleration_x")
    public String acceleration_x;

    @SerializedName("acceleration_y")
    public String acceleration_y;

    @SerializedName("acceleration_z")
    public String acceleration_z;

    @SerializedName("ride_id")
    public int ride_id;

    @SerializedName("start_time")
    public float start_time;

    @SerializedName("end_time")
    public float end_time;

    public Reading(HashMap reading) {

//        double start_lat, double start_lon, double end_lat, double end_lon, String acceleration_x, String acceleration_y,
//                String acceleration_z, int ride_id
        this.start_lat = start_lat;
        this.start_lon = start_lon;
        this.end_lat = end_lat;
        this.end_lon = end_lon;
        this.acceleration_x = acceleration_x;
        this.acceleration_y = acceleration_y;
        this.acceleration_z = acceleration_z;
        this.ride_id = ride_id;
    }

}
