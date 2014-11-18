package com.thomasdh.trafficsimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.thomasdh.trafficsimulation.objects.FollowTheLeaderCar;
import com.thomasdh.trafficsimulation.objects.SimulationSettings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Thomas on 14-11-2014 in project TrafficSimulation.
 */
public class FollowTheLeaderSimulation implements Simulation {

    String progressFilePath = "progress.txt";

    float screenWidth = 1, screenHeight = 1;

    ArrayList<FollowTheLeaderCar> cars;
    SimulationSettings settings;

    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;

    float meanSpeed;
    float standardDeviation;

    boolean running = false;

    @Override
    public void setup() {
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(screenWidth, screenHeight);
        camera.translate(screenWidth / 2f, screenHeight / 2f);

        setRunning(false);

        if (Gdx.files.local(progressFilePath).exists()) {
            resumeSimulation();
        } else {
            startSimulation(getDefaultSettings());
        }
    }

    public SimulationSettings getDefaultSettings() {
        SimulationSettings simulationSettings = new SimulationSettings();
        simulationSettings.setRoadLength(200);
        simulationSettings.setNumberOfCars(100);
        simulationSettings.setSimulationsPerSecond(50);
        simulationSettings.setSpeedMultiplier(1f);
        simulationSettings.setNumberOfLanes(10);
        simulationSettings.setLaneWidth(0.03f);
        simulationSettings.setMinDistance(0.1f);
        simulationSettings.setA(1);
        return simulationSettings;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void resumeSimulation() {
        System.out.println("Resuming simulation");
        FileHandle handle = Gdx.files.local(progressFilePath);
        try {
            ObjectInputStream stream = new ObjectInputStream(handle.read());
            settings = (SimulationSettings) stream.readObject();
            cars = (ArrayList<FollowTheLeaderCar>) stream.readObject();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            startSimulation(getDefaultSettings());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            startSimulation(getDefaultSettings());
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
        cars.get(settings.getNumberOfCars() / 2).setPosition(cars.get(settings.getNumberOfCars() / 2).getPosition() + 0.1f);
    }

    @Override
    public void simulate(float delta) {
        if (running) {
            meanSpeed = 0f;
            standardDeviation = 0f;

            for (int x = 0; x < settings.getNumberOfCars(); x++) {
                FollowTheLeaderCar thisOne = cars.get(x);
                thisOne.setPreviousSpeed(thisOne.getSpeed());
                thisOne.setSpeed(thisOne.getSpeed() + thisOne.getAcceleration() * settings.getSimulationTickTime());
                thisOne.setPreviousPosition(thisOne.getPosition());
                thisOne.setPosition((thisOne.getPosition() + thisOne.getSpeed() * settings.getSimulationTickTime()) % settings.getRoadLength());
                meanSpeed += thisOne.getSpeed() / settings.getNumberOfCars();
            }
            for (int x = 0; x < settings.getNumberOfCars(); x++) {
                FollowTheLeaderCar thisOne = cars.get(x);
                FollowTheLeaderCar nextOne = cars.get((x + 1) % settings.getNumberOfCars());
                float positionDifference = (float) (nextOne.getPreviousPosition() - nextOne.getWidth() - thisOne.getPreviousPosition());
                if (positionDifference < 0) {
                    positionDifference += settings.getRoadLength();
                }

                positionDifference -= settings.getMinDistance();

                thisOne.setAcceleration(settings.getA() * (getWantedSpeed(positionDifference) - thisOne.getPreviousSpeed()));

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
            shapeRenderer.rect(0, screenHeight / (float) (settings.getNumberOfLanes() + 1) * (x + 1) - settings.getLaneWidth() / 2f, screenWidth, settings.getLaneWidth());
        }

        for (FollowTheLeaderCar car : cars) {
            shapeRenderer.setColor(car.getColor(meanSpeed));
            shapeRenderer.rect((float) car.getX(), (float) car.getY(), (float) car.getWidth(), (float) car.getHeight());
        }

        shapeRenderer.end();
    }

    @Override
    public void finish() {
        if (Gdx.files.isLocalStorageAvailable()) {
            FileHandle handle = Gdx.files.local(progressFilePath);
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(handle.write(false));
                outputStream.writeObject(settings);
                outputStream.writeObject(cars);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    float getWantedSpeed(float distance) {
        return (float) (Math.tanh(distance - 2f) + Math.tanh(2f));
    }

    float getMeanSpeed() {
        return meanSpeed;
    }

    float getStandardDeviation() {
        return standardDeviation;
    }

}
