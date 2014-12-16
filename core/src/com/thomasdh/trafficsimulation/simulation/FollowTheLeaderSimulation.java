package com.thomasdh.trafficsimulation.simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.thomasdh.trafficsimulation.objects.FollowTheLeaderCar;
import com.thomasdh.trafficsimulation.objects.Presets;
import com.thomasdh.trafficsimulation.objects.SimulationSettings;
import com.thomasdh.trafficsimulation.storage.Progress;
import com.thomasdh.trafficsimulation.storage.ProgressSaver;

import java.util.ArrayList;

/**
 * Created by Thomas on 14-11-2014 in project TrafficSimulation.
 */
public class FollowTheLeaderSimulation {

    private final float screenWidth = 1;
    private final float screenHeight = 1;

    private ArrayList<FollowTheLeaderCar> cars;

    public SimulationSettings getSettings() {
        return settings;
    }

    private SimulationSettings settings;

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private final ProgressSaver progressSaver;

    public FollowTheLeaderSimulation(ProgressSaver progressSaver) {
        this.progressSaver = progressSaver;
    }

    private float meanSpeed;
    private float standardDeviation;

    public boolean running = false;

    public void setSettings(SimulationSettings settings) {
        this.settings = settings;
    }

    public void setup() {
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(screenWidth, screenHeight);
        camera.translate(screenWidth / 2f, screenHeight / 2f);

        setRunning(false);
        resumeSimulation();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    void resumeSimulation() {
        System.out.println("Resuming simulation");

        Progress progress = progressSaver.readProgress();

        if (progress == null) {
            if (settings == null) {
                startSimulation(Presets.defaultPresets[0].getSettings().copy());
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

        time = 0;

        this.settings = settings;

        cars = new ArrayList<FollowTheLeaderCar>();
        for (int x = 0; x < settings.getNumberOfCars(); x++) {
            cars.add(new FollowTheLeaderCar(screenWidth, screenHeight, settings.getRoadLength() / settings.getNumberOfCars() * x, 0f, settings.getNumberOfLanes(), settings.getLaneLength()));
        }
        cars.get(settings.getNumberOfCars() / 2).setPosition(cars.get(settings.getNumberOfCars() / 2).getPosition() + settings.getInitialFluctuation());
    }

    float time;
    float maxspeed;
    float minspeed;

    public void simulate(float delta) {
        if (running) {
            time += settings.getSimulationTickTime();

            meanSpeed = 0f;
            standardDeviation = 0f;
            maxspeed = 0f;
            minspeed = Float.MAX_VALUE;

            for (int x = 0; x < settings.getNumberOfCars(); x++) {
                FollowTheLeaderCar thisOne = cars.get(x);
                thisOne.setSpeed(Math.max(0, thisOne.getSpeed() + thisOne.getAcceleration() * settings.getSimulationTickTime()));
                thisOne.setPosition((thisOne.getPosition() + thisOne.getSpeed() * settings.getSimulationTickTime()) % settings.getRoadLength());
                meanSpeed += thisOne.getSpeed() / settings.getNumberOfCars();
                maxspeed = Math.max(thisOne.getSpeed(), maxspeed);
                minspeed = Math.min(thisOne.getSpeed(), minspeed);
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
                Thread.sleep(Math.max(0, (long) ((settings.getTickTime() - Gdx.graphics.getDeltaTime()) / settings.getSpeedMultiplier() * 1000f)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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

    public void finish() {
        Progress progress = new Progress();
        progress.cars = cars;
        progress.settings = settings;
        progressSaver.saveProgress(progress);
    }

    public float getMeanSpeed() {
        return meanSpeed + 0.0000001f;
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
