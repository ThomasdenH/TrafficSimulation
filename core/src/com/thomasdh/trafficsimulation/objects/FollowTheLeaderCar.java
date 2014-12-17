package com.thomasdh.trafficsimulation.objects;

import com.badlogic.gdx.graphics.Color;

import java.io.Serializable;

/**
 * Created by Thomas on 14-11-2014 in project TrafficSimulation in project ${PROJECT_NAME}.
 */
public class FollowTheLeaderCar implements Serializable {
    private float position;
    private float speed;
    private float acceleration;
    private final float laneLength;
    private final int numberOfLanes;
    private final float screenWidth;
    private final float screenHeight;

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public FollowTheLeaderCar(float screenWidth, float screenHeight, float position, int numberOfLanes, float laneLength) {
        this.position = position;
        this.speed = 0f;
        this.numberOfLanes = numberOfLanes;
        this.laneLength = laneLength;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public double getRealWidth() {
        return 4;
    }

    double getRealHeight() {
        return 2;
    }

    public double getScreenWidth() {
        return getRealWidth() / laneLength * screenWidth;
    }

    public double getScreenHeight(){
        return getRealHeight() / laneLength * screenWidth;
    }

    public double getY() {
        double distancePerLane = screenHeight / (numberOfLanes + 1);
        return distancePerLane * (1 + Math.floor((position) / laneLength)) - getScreenHeight() / 2f;
    }

    public double getX() {
        return (position % laneLength) / laneLength * screenWidth - getScreenWidth() / 2f;
    }

    public Color getColor(float meanSpeed) {
        meanSpeed += 0.001f;
        return new Color(1f - getSpeed() / meanSpeed, getSpeed() / meanSpeed, 0f, 1f);
    }
}
