package com.thomasdh.trafficsimulation.objects;

/**
 * Created by Thomas on 23-11-2014 in project TrafficSimulation.
 */
public class Presets {

    public static class Preset {
        public SimulationSettings getSettings() {
            return settings;
        }

        public String getName() {
            return name;
        }

        final SimulationSettings settings;
        final String name;

        public Preset(String name, SimulationSettings settings) {
            this.name = name;
            this.settings = settings;
        }
    }

    public static final Preset[] defaultPresets = new Preset[]{
            new Preset("Default", new SimulationSettings() {{
                setRoadLength(1000);
                setNumberOfCars(150);
                setSimulationsPerSecond(30);
                setSpeedMultiplier(1);
                setNumberOfLanes(8);
                setJamDistanceSZero(2);
                setJamDistanceSOne(0);
                setAccelerationA(1f);
                setDecelerationB(3f);
                setMaxSpeed(33);
                setT(0.8f);
                setDelta(4f);
                setLaneWidth(3);
                setInitialFluctuation(1f);
            }}),

            new Preset("Small road", new SimulationSettings() {{
                setRoadLength(100);
                setNumberOfCars(10);
                setSimulationsPerSecond(30);
                setSpeedMultiplier(1);
                setNumberOfLanes(1);
                setJamDistanceSZero(2);
                setJamDistanceSOne(0);
                setAccelerationA(0.73f);
                setDecelerationB(1.67f);
                setMaxSpeed(33);
                setT(1.6f);
                setDelta(4f);
                setLaneWidth(3);
                setInitialFluctuation(0.3f);
            }}),

            new Preset("Long road", new SimulationSettings(){{
                setRoadLength(2000);
                setNumberOfCars(100);
                setSimulationsPerSecond(30);
                setSpeedMultiplier(1);
                setNumberOfLanes(15);
                setJamDistanceSZero(2);
                setJamDistanceSOne(0);
                setAccelerationA(0.73f);
                setDecelerationB(1.67f);
                setMaxSpeed(33);
                setT(1.6f);
                setDelta(4f);
                setLaneWidth(3);
                setInitialFluctuation(1f);
            }}),

            new Preset("Quick jam", new SimulationSettings(){{
                setRoadLength(500);
                setNumberOfCars(50);
                setSimulationsPerSecond(30);
                setSpeedMultiplier(1);
                setNumberOfLanes(8);
                setJamDistanceSZero(2);
                setJamDistanceSOne(0);
                setAccelerationA(1f);
                setDecelerationB(3f);
                setMaxSpeed(30);
                setT(0.8f);
                setDelta(4f);
                setLaneWidth(3);
                setInitialFluctuation(2f);
            }})
    };

}
