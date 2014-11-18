package com.thomasdh.trafficsimulation.objects;

import java.util.Random;

/**
 * Created by Thomas on 23-9-2014 in project TrafficSimulation.
 */
public class TwoSecondsCar extends RandomizedCar {

    public TwoSecondsCar(double screenWidth, double screenHeight, int numberOfLanes, double startPosition) {
        super(screenWidth, screenHeight, numberOfLanes, startPosition);
    }

    @Override
    public double getWantedSpeed(double delta, BaseCar next) {
        return Math.min(getDefaultSpeed(),
                // Back side of previous car
                ((getPositionOfNext(next) - next.getWidth() / 2f) -
                        // Front side of this car
                        (position + getWidth() / 2f)) / 2f);
    }

    Random random = new Random();

    double getVariation() {
        if (random == null)
            random = new Random();
        return random.nextFloat() + 0.5f;
    }
}
