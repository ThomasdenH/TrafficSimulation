package com.thomasdh.trafficsimulation.objects;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

/**
 * Created by Thomas on 23-9-2014 in project TrafficSimulation.
 */
public abstract class RandomizedCar extends Car {

    double currentVariation = 0f;
    double nextVariation = 0f;
    int progress = 0;
    int interval = 1000;

    public RandomizedCar(double screenWidth, double screenHeight, int numberOfLanes, double startPosition) {
        super(screenWidth, screenHeight, numberOfLanes, startPosition);
    }

    @Override
    double getRandomizedSpeed(double speed) {
        if (progress >= interval) {
            currentVariation = nextVariation;
            nextVariation = 0f;
            progress = 0;
        }
        if (currentVariation == 0f) {
            currentVariation = getVariation();
        }
        if (nextVariation == 0f) {
            nextVariation = getVariation();
        }
        double partDone = (progress / (double) interval);
        return speed * (nextVariation * partDone + currentVariation * (1f - partDone));
    }

    abstract double getVariation();

    @Override
    public Color getColor() {
        return new Color(1f - (float) (speed / getDefaultSpeed()), (float) (speed / getDefaultSpeed()), 1f, 1);
    }
}
