package com.thomasdh.trafficsimulation.objects;

import java.io.Serializable;

/**
 * Created by Thomas on 18-11-2014 in project TrafficSimulation.
 */
public class SimulationSettings implements Serializable {
    private int numberOfCars;
    private int numberOfLanes;
    private float roadLength;
    private float initialFluctuation;
    private float laneWidth;
    private float delta;
    private float maxSpeed;
    private float T;
    private float accelerationA;
    private float decelerationB;
    private int simulationsPerSecond;
    private float speedMultiplier;
    private float jamDistanceSZero;
    private float jamDistanceSOne;

    public SimulationSettings copy(){
        SimulationSettings settings = new SimulationSettings();
        settings.numberOfCars = numberOfCars;
        settings.numberOfLanes = numberOfLanes;
        settings.roadLength = roadLength;
        settings.initialFluctuation = initialFluctuation;
        settings.laneWidth = laneWidth;
        settings.delta = delta;
        settings.maxSpeed = maxSpeed;
        settings.T = T;
        settings.accelerationA = accelerationA;
        settings.decelerationB = decelerationB;
        settings.simulationsPerSecond = simulationsPerSecond;
        settings.speedMultiplier = speedMultiplier;
        settings.jamDistanceSZero = jamDistanceSZero;
        settings.jamDistanceSOne = jamDistanceSOne;
        return settings;
    }



    public void setLaneWidth(float laneWidth) {
        this.laneWidth = laneWidth;
    }

    public float getScreenLaneWidth() {
        return laneWidth / (roadLength / numberOfLanes);
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getT() {
        return T;
    }

    public void setT(float t) {
        T = t;
    }

    public float getAccelerationA() {
        return accelerationA;
    }

    public void setAccelerationA(float accelerationA) {
        this.accelerationA = accelerationA;
    }

    public float getDecelerationB() {
        return decelerationB;
    }

    public void setDecelerationB(float decelerationB) {
        this.decelerationB = decelerationB;
    }

    public float getJamDistanceSZero() {
        return jamDistanceSZero;
    }

    public void setJamDistanceSZero(float jamDistanceSZero) {
        this.jamDistanceSZero = jamDistanceSZero;
    }

    public float getJamDistanceSOne() {
        return jamDistanceSOne;
    }

    public void setJamDistanceSOne(float jamDistanceSOne) {
        this.jamDistanceSOne = jamDistanceSOne;
    }

    public float getDelta() {
        return delta;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

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

    public int getSimulationsPerSecond() {
        return simulationsPerSecond;
    }

    public void setSimulationsPerSecond(int simulationsPerSecond) {
        this.simulationsPerSecond = simulationsPerSecond;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public float getInitialFluctuation() {
        return initialFluctuation;
    }

    public void setInitialFluctuation(float initialFluctuation) {
        this.initialFluctuation = initialFluctuation;
    }

    public float getLaneLength() {
        return roadLength / numberOfLanes;
    }

    public float getSimulationTickTime() {
        return 1f / simulationsPerSecond;
    }

    public float getTickTime() {
        return 1f / getSimulationsPerSecond() * speedMultiplier;
    }
}
