package com.thomasdh.trafficsimulation.objects;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Thomas on 23-9-2014 in project TrafficSimulation.
 */
public abstract class Car extends BaseCar {

    double speed;

    double getDefaultSpeed() {
        return 0.3;
    }

    public Car(double screenWidth, double screenHeight, int numberOfLanes, double startPosition) {
        super(screenWidth, screenHeight, numberOfLanes, startPosition);
    }

    @Override
    public void move(double delta, BaseCar next) {
        double wantedSpeed = getWantedSpeed(delta, next);

        // Try to go to the wanted speed
        if (wantedSpeed > speed) {
            speed = Math.min(wantedSpeed, speed + getMaxAcceleration() * delta);
        } else {
            speed = Math.max(Math.max(0, wantedSpeed), speed - getMaxAcceleration() * delta);
        }

        // Set the position with the speed
        position += delta * getRandomizedSpeed(speed);
        position %= maxDistance;
    }

    double getRandomizedSpeed(double speed) {
        return speed;
    }

    public abstract double getWantedSpeed(double delta, BaseCar car);

    public double getMaxAcceleration() {
        return 0.2;
    }
}
