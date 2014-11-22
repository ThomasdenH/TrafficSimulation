package com.thomasdh.trafficsimulation.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.thomasdh.trafficsimulation.objects.FollowTheLeaderCar;
import com.thomasdh.trafficsimulation.objects.SimulationSettings;
import com.thomasdh.trafficsimulation.storage.Progress;
import com.thomasdh.trafficsimulation.storage.ProgressSaver;

import java.util.ArrayList;

/**
 * Created by Thomas on 14-11-2014 in project TrafficSimulation.
 */
public class FollowTheLeaderSimulation implements Simulation {

    float screenWidth = 1, screenHeight = 1;

    ArrayList<FollowTheLeaderCar> cars;

    public SimulationSettings getSettings() {
        return settings;
    }

    SimulationSettings settings;

    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;

    ProgressSaver progressSaver;

    public FollowTheLeaderSimulation(ProgressSaver progressSaver) {
        this.progressSaver = progressSaver;
    }

    float meanSpeed;
    float standardDeviation;

    public boolean running = false;

    public void setSettings(SimulationSettings settings) {
        this.settings = settings;
    }

    @Override
    public void setup() {
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(screenWidth, screenHeight);
        camera.translate(screenWidth / 2f, screenHeight / 2f);

        setRunning(false);
        resumeSimulation();
    }

    public static SimulationSettings getDefaultSettings() {
        SimulationSettings simulationSettings = new SimulationSettings();
        simulationSettings.setRoadLength(100);
        simulationSettings.setNumberOfCars(15);
        simulationSettings.setSimulationsPerSecond(20);
        simulationSettings.setSpeedMultiplier(0.01f);
        simulationSettings.setNumberOfLanes(1);
        simulationSettings.setJamDistanceSZero(2f);
        simulationSettings.setJamDistanceSOne(0f);
        simulationSettings.setAccelerationA(0.3f);
        simulationSettings.setDecelerationB(0.3f);
        simulationSettings.setMaxSpeed(20);
        simulationSettings.setT(1.6f);
        simulationSettings.setDelta(4);
        simulationSettings.setLaneWidth(3);
        simulationSettings.setInitialFluctuation(0.1f);
        return simulationSettings;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void resumeSimulation() {
        System.out.println("Resuming simulation");

        Progress progress = progressSaver.readProgress();

        if (progress == null) {
            if (settings == null) {
                startSimulation(getDefaultSettings());
            } else {
                startSimulation(settings);
            }
        } else {
            settings = progress.settings;
            cars = progress.cars;
        }
    }

    public void startSimulation(SimulationSettings settings) {
        setRunning(false);
        System.out.println("Starting simulation");

        this.settings = settings;

        cars = new ArrayList<FollowTheLeaderCar>();
        for (int x = 0; x < settings.getNumberOfCars(); x++) {
            cars.add(new FollowTheLeaderCar(screenWidth, screenHeight, settings.getRoadLength() / settings.getNumberOfCars() * x, 0f, settings.getNumberOfLanes(), settings.getLaneLength()));
        }
        cars.get(settings.getNumberOfCars() / 2).setPosition(cars.get(settings.getNumberOfCars() / 2).getPosition() + settings.getInitialFluctuation());
    }

    @Override
    public void simulate(float delta) {
        if (running) {
            meanSpeed = 0f;
            standardDeviation = 0f;

            for (int x = 0; x < settings.getNumberOfCars(); x++) {
                FollowTheLeaderCar thisOne = cars.get(x);
                thisOne.setSpeed(thisOne.getSpeed() + thisOne.getAcceleration() * settings.getSimulationTickTime());
                thisOne.setPosition((thisOne.getPosition() + thisOne.getSpeed() * settings.getSimulationTickTime()) % settings.getRoadLength());
                meanSpeed += thisOne.getSpeed() / settings.getNumberOfCars();
            }
            for (int x = 0; x < settings.getNumberOfCars(); x++) {
                FollowTheLeaderCar thisOne = cars.get(x);
                FollowTheLeaderCar nextOne = cars.get((x + 1) % settings.getNumberOfCars());

                float positionDifference = (float) (nextOne.getPosition() - nextOne.getRealWidth() - thisOne.getPosition());
                if (positionDifference < 0) {
                    positionDifference += settings.getRoadLength();
                }

                float firstPart = (float) Math.pow(thisOne.getSpeed() / settings.getMaxSpeed(), settings.getDelta());
                float secondPart = (float) Math.pow(
                        getWantedDistance(thisOne.getSpeed(), thisOne.getSpeed() - nextOne.getSpeed())
                                / positionDifference,
                        2f);

                thisOne.setAcceleration(settings.getAccelerationA() * (1f - firstPart - secondPart));

                standardDeviation += Math.pow(thisOne.getSpeed() - meanSpeed, 2);
            }

            standardDeviation = (float) Math.sqrt(standardDeviation / settings.getNumberOfCars());

            try {
                Thread.sleep((long) (settings.getTickTime() - Gdx.graphics.getDeltaTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw() {
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (float x = 0; x < settings.getNumberOfLanes(); x++) {
            shapeRenderer.rect(0, screenHeight / (float) (settings.getNumberOfLanes() + 1) * (x + 1) - settings.getScreenLaneWidth() / 2f, screenWidth, settings.getScreenLaneWidth());
        }

        for (FollowTheLeaderCar car : cars) {
            shapeRenderer.setColor(car.getColor(meanSpeed));
            shapeRenderer.rect((float) car.getX(), (float) car.getY(), (float) car.getScreenWidth(), (float) car.getScreenHeight());
        }

        shapeRenderer.end();
    }

    @Override
    public void finish() {
        Progress progress = new Progress();
        progress.cars = cars;
        progress.settings = settings;
        progressSaver.saveProgress(progress);
    }

    public float getMeanSpeed() {
        return meanSpeed;
    }

    public float getStandardDeviation() {
        return standardDeviation;
    }

    float getWantedDistance(float speed, float speedDifference) {
        return (float) (
                // Minimum distance
                settings.getJamDistanceSZero() +


                        Math.max(0,
                                // Zero in my simulation
                                settings.getJamDistanceSOne() * Math.sqrt(speed / settings.getMaxSpeed()) +
                                        settings.getT() * speed

                                        + speed * speedDifference / (2f * Math.sqrt(settings.getAccelerationA() * settings.getDecelerationB()))));
    }

}
