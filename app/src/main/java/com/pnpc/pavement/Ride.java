package com.pnpc.pavement;

/**
 * Created by jjshin on 3/17/16.
 */
public class Ride {

    public float start_time;
    public float end_time;
    public int calibration_id;
    public int scoreboard_id;
    public int id;

    public Ride(){

    }

    public float getStartTime() {
        return start_time;
    }

    public void setStartTime(float start_time) {
        this.start_time = start_time;
    }

    public float getEndTime() {
        return end_time;
    }

    public void setEndTime(float end_time) {
        this.end_time = end_time;
    }

    public int getCalibrationId() {
        return calibration_id;
    }

    public void setCalibrationId(int calibration_id) {
        this.calibration_id = calibration_id;
    }

    public int getScoreboardId() {
        return scoreboard_id;
    }

    public void setScoreboardId(int scoreboard_id) {
        this.scoreboard_id = scoreboard_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
