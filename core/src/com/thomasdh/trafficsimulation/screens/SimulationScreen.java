package com.thomasdh.trafficsimulation.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.thomasdh.trafficsimulation.TrafficSimulationMain;
import com.thomasdh.trafficsimulation.objects.Presets;
import com.thomasdh.trafficsimulation.simulation.FollowTheLeaderSimulation;

/**
 * Created by Thomas on 19-11-2014 in project TrafficSimulation in project ${PROJECT_NAME}.
 */
public class SimulationScreen implements Screen {

    private FollowTheLeaderSimulation simulation;

    private Stage stage;

    @SuppressWarnings("FieldCanBeLocal")
    private final float minWidth = 700f;
    @SuppressWarnings("FieldCanBeLocal")
    private final float minHeight = 700f;

    private Skin uiSkin;

    private Label meanSpeedLabel;
    private Label deviantLabel;

    private final TrafficSimulationMain main;

    public SimulationScreen(TrafficSimulationMain main) {
        this.main = main;
    }

    @Override
    public void show() {
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ExtendViewport(minWidth, minHeight));

        TextButton textButton = new TextButton("Info", uiSkin);
        stage.addActor(textButton);
        textButton.setPosition(10f, 10f);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(generateInfoWindow());
            }
        });

        TextButton settingsButton = new TextButton("Settings", uiSkin);
        stage.addActor(settingsButton);
        settingsButton.setPosition(textButton.getX() + textButton.getWidth() + 10f, 10f);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(TrafficSimulationMain.SCREEN_SETTINGS);
            }
        });

        Gdx.input.setInputProcessor(stage);

        simulation = new FollowTheLeaderSimulation(main.progressSaver);
        simulation.setSettings(main.getCurrentSettings());
        simulation.setup();
    }

    @Override
    public void render(float delta) {
        if (meanSpeedLabel != null) {
            meanSpeedLabel.setText("Mean speed: " + Math.round(simulation.getMeanSpeed() * 10000f) / 10000f);
        }
        if (deviantLabel != null) {
            deviantLabel.setText("Standard deviation: " + Math.round(simulation.getStandardDeviation() * 10000f) / 10000f);
        }

        simulation.simulate(Gdx.graphics.getDeltaTime());
        stage.act();

        Gdx.gl.glClearColor(0.3f, 0.8f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        simulation.draw();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    Window generateInfoWindow() {
        final Window iWindow = new Window("Info", uiSkin);
        iWindow.setPosition(10f, 10f);
        iWindow.setSize(350f, 120f);

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
                simulation.startSimulation(main.getCurrentSettings() == null ? Presets.defaultPresets[0].getSettings().copy() : main.getCurrentSettings());
            }
        });

        final TextButton start = new TextButton("Start", uiSkin);
        start.setPosition(120f, iWindow.getHeight() - 110f);
        start.setSize(100f, 30f);
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (simulation.running) {
                    simulation.setRunning(false);
                    start.setText("Start");
                } else {
                    simulation.setRunning(true);
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

    @Override
    public void hide() {
        simulation.finish();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
