package com.thomasdh.trafficsimulation.simulation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.thomasdh.trafficsimulation.objects.FollowTheLeaderCar;
import com.thomasdh.trafficsimulation.objects.Presets;
import com.thomasdh.trafficsimulation.objects.SimulationSettings;
import com.thomasdh.trafficsimulation.storage.Progress;
import com.thomasdh.trafficsimulation.storage.ProgressSaver;

import java.util.ArrayList;

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

    private boolean running = false;

    public void setSettings(SimulationSettings settings) {
        this.settings = settings;
    }

    public void setup() {
        setShapeRenderer(new ShapeRenderer());
        setCamera(new OrthographicCamera(getScreenWidth(), getScreenHeight()));
        getCamera().translate(getScreenWidth() / 2f, getScreenHeight() / 2f);

        setRunning(false);
        resumeSimulation();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    void resumeSimulation() {
        System.out.println("Resuming simulation");

        Progress progress = getProgressSaver().readProgress();

        if (progress == null) {
            if (getSettings() == null) {
                startSimulation(Presets.defaultPresets[0].getSettings().copy());
            } else {
                startSimulation(getSettings());
            }
        } else {
            setSettings(progress.settings);
            setCars(progress.cars);
        }
    }

    public void startSimulation(SimulationSettings settings) {
        setRunning(false);
        System.out.println("Starting simulation");

        this.setSettings(settings);

        setCars(new ArrayList<FollowTheLeaderCar>());
        for (int x = 0; x < settings.getNumberOfCars(); x++) {
            getCars().add(new FollowTheLeaderCar(getScreenWidth(), getScreenHeight(), settings.getRoadLength() / settings.getNumberOfCars() * x, settings.getNumberOfLanes(), settings.getLaneLength()));
        }
        getCars().get(settings.getNumberOfCars() / 2).setPosition(getCars().get(settings.getNumberOfCars() / 2).getPosition() + settings.getInitialFluctuation());
    }

    private long simulationTime;
    private long realTime;

    private final long maximumFrameTimeMS = 20;

    public void simulate(float delta) {


        if (isRunning()) {

            long frameStartTime = System.currentTimeMillis();
            setRealTime((long) (getRealTime() + delta * 1000f));

            while (getRealTime() > getSimulationTime() + 1000f * getSettings().getSimulationTickTime() / getSettings().getSpeedMultiplier()
                    && System.currentTimeMillis() - frameStartTime < getMaximumFrameTimeMS()) {

                setSimulationTime((long) (getSimulationTime() + 1000f * getSettings().getSimulationTickTime() / getSettings().getSpeedMultiplier()));

                setMeanSpeed(0f);
                setStandardDeviation(0f);

                for (int x = 0; x < getSettings().getNumberOfCars(); x++) {
                    FollowTheLeaderCar thisOne = getCars().get(x);
                    thisOne.setSpeed(Math.max(0, thisOne.getSpeed() + thisOne.getAcceleration() * getSettings().getSimulationTickTime()));
                    thisOne.setPosition((thisOne.getPosition() + thisOne.getSpeed() * getSettings().getSimulationTickTime()) % getSettings().getRoadLength());
                    setMeanSpeed(getMeanSpeed() + thisOne.getSpeed() / getSettings().getNumberOfCars());
                }
                for (int x = 0; x < getSettings().getNumberOfCars(); x++) {
                    FollowTheLeaderCar thisOne = getCars().get(x);
                    FollowTheLeaderCar nextOne = getCars().get((x + 1) % getSettings().getNumberOfCars());

                    float positionDifference = (float) (nextOne.getPosition() - nextOne.getRealWidth() - thisOne.getPosition());
                    if (positionDifference < 0) {
                        positionDifference += getSettings().getRoadLength();
                    }

                    float firstPart = (float) Math.pow(thisOne.getSpeed() / getSettings().getMaxSpeed(), getSettings().getDelta());
                    float secondPart = (float) Math.pow(
                            getWantedDistance(thisOne.getSpeed(), thisOne.getSpeed() - nextOne.getSpeed())
                                    / positionDifference,
                            2f);

                    thisOne.setAcceleration(getSettings().getAccelerationA() * (1f - firstPart - secondPart));

                    setStandardDeviation((float) (getStandardDeviation() + Math.pow(thisOne.getSpeed() - getMeanSpeed(), 2)));
                }

                setStandardDeviation((float) Math.sqrt(getStandardDeviation() / getSettings().getNumberOfCars()));
            }
        }
    }

    public void draw() {
        getCamera().update();
        getShapeRenderer().setProjectionMatrix(getCamera().combined);

        getShapeRenderer().setColor(Color.GRAY);
        getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);

        for (float x = 0; x < getSettings().getNumberOfLanes(); x++) {
            getShapeRenderer().rect(0, getScreenHeight() / (float) (getSettings().getNumberOfLanes() + 1) * (x + 1) - getSettings().getScreenLaneWidth() / 2f, getScreenWidth(), getSettings().getScreenLaneWidth());
        }

        for (FollowTheLeaderCar car : getCars()) {
            getShapeRenderer().setColor(car.getColor(getMeanSpeed()));
            getShapeRenderer().rect((float) car.getX(), (float) car.getY(), (float) car.getScreenWidth(), (float) car.getScreenHeight());
        }

        getShapeRenderer().end();
    }

    public void finish() {
        Progress progress = new Progress();
        progress.cars = getCars();
        progress.settings = getSettings();
        getProgressSaver().saveProgress(progress);
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
                getSettings().getJamDistanceSZero() +


                        Math.max(0,
                                // Zero in my simulation
                                getSettings().getJamDistanceSOne() * Math.sqrt(speed / getSettings().getMaxSpeed()) +
                                        getSettings().getT() * speed

                                        + speed * speedDifference / (2f * Math.sqrt(getSettings().getAccelerationA() * getSettings().getDecelerationB()))));
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public ArrayList<FollowTheLeaderCar> getCars() {
        return cars;
    }

    public void setCars(ArrayList<FollowTheLeaderCar> cars) {
        this.cars = cars;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public void setShapeRenderer(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public ProgressSaver getProgressSaver() {
        return progressSaver;
    }

    public void setMeanSpeed(float meanSpeed) {
        this.meanSpeed = meanSpeed;
    }

    public void setStandardDeviation(float standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public boolean isRunning() {
        return running;
    }

    public long getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(long simulationTime) {
        this.simulationTime = simulationTime;
    }

    public long getRealTime() {
        return realTime;
    }

    public void setRealTime(long realTime) {
        this.realTime = realTime;
    }

    public long getMaximumFrameTimeMS() {
        return maximumFrameTimeMS;
    }
}
