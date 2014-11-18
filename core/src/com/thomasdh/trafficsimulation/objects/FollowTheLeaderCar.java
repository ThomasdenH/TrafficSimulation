package com.thomasdh.trafficsimulation.objects;

import com.badlogic.gdx.graphics.Color;

import java.io.Serializable;

/**
 * Created by Thomas on 14-11-2014 in project TrafficSimulation.
 */
public class FollowTheLeaderCar implements Serializable {
    float position, speed, acceleration;
    float laneLength;
    int numberOfLanes;
    float screenWidth, screenHeight;

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    int round = 0;

    float previousPosition;

    public void setPreviousSpeed(float previousSpeed) {
        this.previousSpeed = previousSpeed;
    }

    public void setPreviousPosition(float previousPosition) {
        this.previousPosition = previousPosition;
    }

    float previousSpeed;

    public float getPreviousSpeed() {
        return previousSpeed;
    }


    public float getPreviousPosition() {
        return previousPosition;
    }

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

    public FollowTheLeaderCar(float screenWidth, float screenHeight, float position, float speed, int numberOfLanes, float laneLength) {
        this.position = position;
        this.speed = speed;
        this.numberOfLanes = numberOfLanes;
        this.laneLength = laneLength;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public double getWidth() {
        return 0.02;
    }

    public double getHeight() {
        return 0.01;
    }

    public double getY() {
        double distancePerLane = screenHeight / (numberOfLanes + 1);
        return distancePerLane * (1 + Math.floor((position) / laneLength)) - getHeight() / 2f;
    }

    public double getX() {
        return (position % laneLength) / laneLength * screenWidth - getWidth() / 2f;
    }

    public Color getColor(float meanSpeed) {
        return new Color(1f - getSpeed() / meanSpeed, getSpeed() / meanSpeed, 0f, 1f);
    }
}
