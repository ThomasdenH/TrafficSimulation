package com.thomasdh.trafficsimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.thomasdh.trafficsimulation.objects.AccellerationCar;
import com.thomasdh.trafficsimulation.objects.BaseCar;
import com.thomasdh.trafficsimulation.objects.Car;
import com.thomasdh.trafficsimulation.objects.SlowCar;

import java.util.ArrayList;

/**
 * Created by Thomas on 23-9-2014 in project TrafficSimulation.
 */
public class JamSimulation implements Simulation {

    int amountOfCars = 20;

    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;

    ArrayList<BaseCar> cars = new ArrayList<BaseCar>();

    final int screenWidth = 1;
    final int screenHeight = 1;

    final int lanes = 10;
    final float laneWidth = 0.05f;

    @Override
    public void setup() {
        float totalDistance = screenWidth * lanes;
        for (int u = 0; u < amountOfCars; u++) {
            cars.add(new AccellerationCar(1, 1, lanes, totalDistance / (float) amountOfCars * (float) (amountOfCars - u)));
        }
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(screenWidth, screenHeight);
        camera.translate(screenWidth / 2f, screenHeight / 2f, 0f);
    }

    @Override
    public void simulate(float delta) {
        if (Gdx.input.isTouched()) {
            delta *= 10;
        }

        for (int x = 0; x < cars.size(); x++) {
            cars.get(x).move(delta, x > 0 ? cars.get(x - 1) : cars.get(cars.size() - 1));
        }
    }

    @Override
    public void finish() {

    }

    @Override
    public void draw() {
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (float x = 0; x < lanes; x++) {
            shapeRenderer.rect(0, (float) screenHeight / (float) (lanes + 1) * (x + 1) - laneWidth / 2f, screenWidth, laneWidth);
        }

        for (BaseCar car : cars) {
            shapeRenderer.setColor(car.getColor());
            shapeRenderer.rect((float) car.getX(), (float) car.getY(), (float) car.getWidth(), (float) car.getHeight());
        }

        shapeRenderer.end();
    }
}
