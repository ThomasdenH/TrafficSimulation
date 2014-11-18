package com.thomasdh.trafficsimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.thomasdh.trafficsimulation.objects.FollowTheLeaderCar;

import java.util.ArrayList;

/**
 * Created by Thomas on 14-11-2014 in project TrafficSimulation.
 */
public class FollowTheLeaderSimulation implements Simulation {

    final int amountOfCars = 60;
    final int simulationsPerSecond = 30;
    float timeScale = 4f;
    final float tickTime = 1f / simulationsPerSecond;

    final int lanes = 10;
    final float roadLength = 100;
    final float laneWidth = 0.03f;

    float minDistance = 0.1f;

    float screenWidth = 1, screenHeight = 1;

    float a = 1f;

    ArrayList<FollowTheLeaderCar> cars;

    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;

    float meanSpeed;

    @Override
    public void setup() {

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(screenWidth, screenHeight);
        camera.translate(screenWidth / 2f, screenHeight / 2f, 0f);

        cars = new ArrayList<FollowTheLeaderCar>();
        for (int x = 0; x < amountOfCars; x++) {
            cars.add(new FollowTheLeaderCar(screenWidth, screenHeight, roadLength / amountOfCars * x, 0f, lanes, roadLength / lanes));
        }
        cars.get(amountOfCars / 2).setPosition(cars.get(amountOfCars / 2).getPosition() + 0.1f);
    }

    @Override
    public void simulate(float delta) {

        meanSpeed = 0f;

        for (int x = 0; x < amountOfCars; x++) {
            FollowTheLeaderCar thisOne = cars.get(x);
            thisOne.setPreviousSpeed(thisOne.getSpeed());
            thisOne.setSpeed(thisOne.getSpeed() + thisOne.getAcceleration() * tickTime);
            thisOne.setPreviousPosition(thisOne.getPosition());
            thisOne.setPosition((thisOne.getPosition() + thisOne.getSpeed() * tickTime) % roadLength);
            meanSpeed += thisOne.getSpeed() / amountOfCars;
        }
        for (int x = 0; x < amountOfCars; x++) {
            FollowTheLeaderCar thisOne = cars.get(x);
            FollowTheLeaderCar nextOne = cars.get((x + 1) % amountOfCars);
            float positionDifference = (float) (nextOne.getPreviousPosition() - nextOne.getWidth() - thisOne.getPreviousPosition());
            if (positionDifference < 0) {
                positionDifference += roadLength;
            }

            positionDifference -= minDistance;

            thisOne.setAcceleration(a * (getWantedSpeed(positionDifference) - thisOne.getPreviousSpeed()));
        }
        if (!Gdx.input.isTouched()) {
            try {
                Thread.sleep((long) (1000 / simulationsPerSecond / timeScale - Gdx.graphics.getDeltaTime()));
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

        for (float x = 0; x < lanes; x++) {
            shapeRenderer.rect(0, screenHeight / (float) (lanes + 1) * (x + 1) - laneWidth / 2f, screenWidth, laneWidth);
        }

        for (FollowTheLeaderCar car : cars) {
            shapeRenderer.setColor(car.getColor(meanSpeed));
            shapeRenderer.rect((float) car.getX(), (float) car.getY(), (float) car.getWidth(), (float) car.getHeight());
        }

        shapeRenderer.end();
    }

    @Override
    public void finish() {

    }

    float getWantedSpeed(float distance) {
        return (float) (Math.tanh(distance - 2f) + Math.tanh(2f));
        //return Math.min(0.5f * distance, defaultSpeed);
    }
}
