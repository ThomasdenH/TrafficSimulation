package com.thomasdh.trafficsimulation.objects;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Thomas on 23-9-2014 in project TrafficSimulation.
 */
public class SlowCar extends RandomizedCar {

    Random random = new Random();

    double visibleDistance = 0.5;
    double noticableSpeedDifference = 0.1;

    public SlowCar(double screenWidth, double screenHeight, int numberOfLanes, double startPosition) {
        super(screenWidth, screenHeight, numberOfLanes, startPosition);
    }

    ArrayList<Intention> intentions = new ArrayList<Intention>();
    final double delay = 0.5;

    double currentTime;

    @Override
    public double getWantedSpeed(double delta, BaseCar next) {
        return calculateWantedSpeedNoDelay(delta, next);
    }


    public double calculateWantedSpeedNoDelay(double delta, BaseCar next) {
        if (getDistanceToNextCar(next) < visibleDistance && Math.abs(next.speed - speed) > noticableSpeedDifference){
            return next.speed;
        } else {
            return getDefaultSpeed();
        }
    }

    class Intention {
        Intention(double time, double speed) {
            this.time = time;
            this.speed = speed;
        }

        double time;
        double speed;
    }

    @Override
    double getVariation() {
        if (random == null)
            random = new Random();
        return random.nextFloat() * 0.2f + 0.8f;
    }

    @Override
    public double getMaxAcceleration() {
        return 2;
    }


}
