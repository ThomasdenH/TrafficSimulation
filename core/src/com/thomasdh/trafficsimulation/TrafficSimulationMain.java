package com.thomasdh.trafficsimulation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class TrafficSimulationMain extends ApplicationAdapter {

    Simulation simulation;

    Stage stage;

    float minWidth = 700f, minHeight = 700f;

    Skin uiSkin;

    Label meanSpeedLabel;
    Label deviantLabel;

    @Override

    public void create() {

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ExtendViewport(minWidth, minHeight));

        TextButton textButton = new TextButton("Settings", uiSkin);
        stage.addActor(textButton);
        textButton.setPosition(10f, 10f);

        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(generateInfoWindow());
            }
        });

        Gdx.input.setInputProcessor(stage);

        simulation = new FollowTheLeaderSimulation();
        simulation.setup();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height);
    }

    @Override
    public void render() {

        if (meanSpeedLabel != null) {
            meanSpeedLabel.setText("Mean speed: " + String.format("%.2f", ((FollowTheLeaderSimulation) simulation).getMeanSpeed()));
        }
        if (deviantLabel != null) {
            deviantLabel.setText("Standard deviation: " + String.format("%.2f", ((FollowTheLeaderSimulation) simulation).getStandardDeviation()));
        }

        simulation.simulate(Gdx.graphics.getDeltaTime());
        stage.act();

        Gdx.gl.glClearColor(0.3f, 0.8f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        simulation.draw();
        stage.draw();
    }

    @Override
    public void dispose() {
        simulation.finish();
        stage.dispose();
        super.dispose();
    }

    Window generateInfoWindow() {
        final Window iWindow = new Window("Scherm", uiSkin);
        iWindow.setPosition(10f, 10f);
        iWindow.setSize(400f, 200f);

        meanSpeedLabel = new Label("Mean speed: ", uiSkin);
        meanSpeedLabel.setPosition(10f, iWindow.getHeight() - 50f);
        meanSpeedLabel.setSize(100f, 30f);

        deviantLabel = new Label("Standard deviation: ", uiSkin);
        deviantLabel.setPosition(10f, iWindow.getHeight() - 80f);
        deviantLabel.setSize(100f, 30f);

        TextButton button = new TextButton("Reset", uiSkin);
        button.setPosition(10f, iWindow.getHeight() - 110f);
        button.setSize(100f, 30f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((FollowTheLeaderSimulation) simulation).startSimulation(((FollowTheLeaderSimulation) simulation).getDefaultSettings());
            }
        });

        final TextButton start = new TextButton("Start", uiSkin);
        start.setPosition(120f, iWindow.getHeight() - 110f);
        start.setSize(100f, 30f);
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (((FollowTheLeaderSimulation) simulation).running) {
                    ((FollowTheLeaderSimulation) simulation).setRunning(false);
                    start.setText("Start");
                } else {
                    ((FollowTheLeaderSimulation) simulation).setRunning(true);
                    start.setText("Pause");
                }
            }
        });

        TextButton close = new TextButton("Close", uiSkin);
        close.setPosition(230f, iWindow.getHeight() - 110f);
        close.setSize(100f, 30f);
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                iWindow.remove();
                iWindow.clear();
            }
        });

        iWindow.addActor(meanSpeedLabel);
        iWindow.addActor(deviantLabel);
        iWindow.addActor(button);
        iWindow.addActor(close);
        iWindow.addActor(start);

        return iWindow;
    }
}
