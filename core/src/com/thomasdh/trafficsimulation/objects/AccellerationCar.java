package com.thomasdh.trafficsimulation.objects;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

/**
 * Created by Thomas on 24-9-2014 in project TrafficSimulation.
 */
public class AccellerationCar extends BaseCar {

    double acelleration;
    double noticibleSpeedDifferenceBoundary = 0.2;
    double acceptableSpeedDifferenceBoundary = 0.05;
    double minNoticingDistance = 0.3;

    boolean noticibleSpeedDifference(double speed1, double speed2) {
        if (Math.abs(speed1 - speed2) > noticibleSpeedDifferenceBoundary) {
            return true;
        }
        return false;
    }

    boolean acceptableSpeedDifference(double speed1, double speed2) {
        return Math.abs(speed1 - speed2) < acceptableSpeedDifferenceBoundary;
    }

    public AccellerationCar(double screenWidth, double screenHeight, int numberOfLanes, double startPosition) {
        super(screenWidth, screenHeight, numberOfLanes, startPosition);
        speed = getDefaultSpeed();
    }

    double getMaxAcceleration() {
        return 0.2;
    }

    double getDefaultSpeed() {
        return 0.4;
    }

    boolean accelerating = false;

    Random random = new Random();


    @Override
    public void move(double delta, BaseCar next) {

        if ((getDistanceToNextCar(next) < minNoticingDistance && noticibleSpeedDifference(speed, next.speed)) || accelerating) {
            System.out.print("Noticible!");
            if (speed > next.speed) {
                acelleration = -getMaxAcceleration();
                accelerating = true;
            } else {
                acelleration = getMaxAcceleration();
                accelerating = true;
            }
        }

        if (acceptableSpeedDifference(speed, next.speed)) {
            acelleration = random.nextFloat() * 0.1 - 0.1;
            accelerating = false;
        }

        speed += delta * acelleration;

        if (speed < 0) {
            speed = 0;
        }

        position += delta * speed;

        position %= maxDistance;
    }

    @Override
    public Color getColor() {
        return new Color(1f - (float) (speed / getDefaultSpeed()), (float) (speed / getDefaultSpeed()), 0f, 1);
    }
}
