package com.thomasdh.trafficsimulation.objects;

import java.io.Serializable;

/**
 * Created by Thomas on 18-11-2014 in project TrafficSimulation.
 */
public class SimulationSettings implements Serializable{
    int numberOfCars;
    int numberOfLanes;
    float roadLength;

    float laneWidth;

    public int getNumberOfCars() {
        return numberOfCars;
    }

    public void setNumberOfCars(int numberOfCars) {
        this.numberOfCars = numberOfCars;
    }

    public int getNumberOfLanes() {
        return numberOfLanes;
    }

    public void setNumberOfLanes(int numberOfLanes) {
        this.numberOfLanes = numberOfLanes;
    }

    public float getRoadLength() {
        return roadLength;
    }

    public void setRoadLength(float roadLength) {
        this.roadLength = roadLength;
    }

    public float getLaneWidth() {
        return laneWidth;
    }

    public void setLaneWidth(float laneWidth) {
        this.laneWidth = laneWidth;
    }

    public float getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(float minDistance) {
        this.minDistance = minDistance;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public int getSimulationsPerSecond() {
        return simulationsPerSecond;
    }

    public void setSimulationsPerSecond(int simulationsPerSecond) {
        this.simulationsPerSecond = simulationsPerSecond;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    float minDistance;

    float a;

    int simulationsPerSecond;
    float speedMultiplier;

    public float getLaneLength() {
        return roadLength / numberOfLanes;
    }

    public float getSimulationTickTime(){
        return 1f / simulationsPerSecond;
    }

    public float getTickTime() {
        return 1f / simulationsPerSecond * speedMultiplier;
    }
}
