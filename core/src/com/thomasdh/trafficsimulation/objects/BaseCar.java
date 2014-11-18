package com.thomasdh.trafficsimulation.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.compression.lzma.Base;

/**
 * Created by Thomas on 24-9-2014 in project TrafficSimulation.
 */
public abstract class BaseCar {

    double speed;

    double position;
    int numberOfLanes;
    double maxDistance;
    double laneLength;
    double screenHeight;

    public BaseCar(double screenWidth, double screenHeight, int numberOfLanes, double startPosition) {
        this.position = startPosition;
        this.numberOfLanes = numberOfLanes;
        this.laneLength = screenWidth + getWidth();
        this.screenHeight = screenHeight;
        this.maxDistance = laneLength * numberOfLanes;
    }

    public abstract void move(double delta, BaseCar next);

    public double getWidth() {
        return 0.04;
    }

    public double getHeight() {
        return 0.02;
    }

    public double getY() {
        double distancePerLane = screenHeight / (numberOfLanes + 1);
        return distancePerLane * (1 + Math.floor((position) / laneLength)) - getHeight() / 2f;
    }

    public double getX() {
        return (position) % laneLength - getWidth();
    }

    public abstract Color getColor();

    double getPositionOfNext(BaseCar next) {
        if (next.position < position) {
            return next.position + maxDistance;
        }
        return next.position;
    }

    double getDistanceToNextCar(BaseCar next) {
        return (getPositionOfNext(next) - next.getWidth() / 2f) - (position + getWidth() / 2f);
    }
}
