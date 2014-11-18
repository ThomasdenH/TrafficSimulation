package com.thomasdh.trafficsimulation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class TrafficSimulationMain extends ApplicationAdapter {

    Simulation simulation;

    @Override
    public void create() {
        simulation = new FollowTheLeaderSimulation();
        simulation.setup();
        lastTime = System.currentTimeMillis();
    }

    long lastTime;

    @Override
    public void render() {
        simulation.simulate(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0.3f, 0.8f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        simulation.draw();
    }
}
