package com.thomasdh.trafficsimulation;

/**
 * Created by Thomas on 23-9-2014 in project TrafficSimulation.
 */
public interface Simulation {
    void setup();

    void simulate(float delta);

    void draw();

    void finish();
}
