package com.thomasdh.trafficsimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.thomasdh.trafficsimulation.objects.SimulationSettings;

import java.text.DecimalFormat;

/**
 * Created by Thomas on 19-11-2014 in project TrafficSimulation.
 */
public class SettingsScreen implements Screen {

    TrafficSimulationMain main;

    Stage stage;
    float minWidth = 700f, minHeight = 700f;
    Skin uiSkin;

    SimulationSettings settings;

    public SettingsScreen(TrafficSimulationMain main) {
        this.main = main;
    }

    public void setSettings(SimulationSettings settings) {
        this.settings = settings;
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }


    @Override
    public void show() {

        if (settings == null)
            settings = FollowTheLeaderSimulation.getDefaultSettings();

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ExtendViewport(minWidth, minHeight));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table(uiSkin);
        table.setSize(stage.getWidth(), stage.getHeight());
        table.setPosition(0, 0);

        final Label label = new Label("Number of cars: " + settings.getNumberOfCars(), uiSkin);
        table.add(label).width(200f);
        final Slider slider = new Slider(1f, 500f, 1f, false, uiSkin);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setNumberOfCars((int) slider.getValue());
                label.setText("Number of cars: " + settings.getNumberOfCars());
            }
        });
        slider.setValue(settings.getNumberOfCars());
        table.add(slider);
        table.row();

        final Label labelNOL = new Label("Number of lanes: " + settings.getNumberOfLanes(), uiSkin);
        table.add(labelNOL).width(200f);
        final Slider sliderNOL = new Slider(1f, 15f, 1f, false, uiSkin);
        sliderNOL.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setNumberOfLanes((int) sliderNOL.getValue());
                labelNOL.setText("Number of lanes: " + settings.getNumberOfLanes());
            }
        });
        sliderNOL.setValue(settings.getNumberOfLanes());
        table.add(sliderNOL);
        table.row();

        final Label labelRL = new Label("Road length: " + settings.getRoadLength(), uiSkin);
        table.add(labelRL).width(200f);
        final Slider sliderRL = new Slider(1f, 500f, 0.5f, false, uiSkin);
        sliderRL.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setRoadLength(sliderRL.getValue());
                labelRL.setText("Road length: " + settings.getRoadLength());
            }
        });
        sliderRL.setValue(settings.getRoadLength());
        table.add(sliderRL);
        table.row();

        final Label labelFLUC = new Label("Initial fluctuation: " + settings.getInitialFluctuation(), uiSkin);
        table.add(labelFLUC).width(200f);
        final Slider sliderFLUC = new Slider(0f, 1f, 0.01f, false, uiSkin);
        sliderFLUC.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setInitialFluctuation(sliderFLUC.getValue());
                labelFLUC.setText("Initial fluctuation: " + new DecimalFormat("#0.00").format(settings.getInitialFluctuation()));
            }
        });
        sliderFLUC.setValue(settings.getInitialFluctuation());
        table.add(sliderFLUC);
        table.row();

        final Label labelA = new Label("A (reaction speed): " + settings.getA(), uiSkin);
        table.add(labelA).width(200f);
        final Slider sliderA = new Slider(0f, 2f, 0.01f, false, uiSkin);
        sliderA.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setA(sliderA.getValue());
                labelA.setText("A (reaction speed): " + new DecimalFormat("#0.00").format(settings.getA()));
            }
        });
        sliderA.setValue(settings.getA());
        table.add(sliderA);
        table.row();

        TextButton button = new TextButton("Save & close", uiSkin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setCurrentSettings(settings);
                main.setScreen(TrafficSimulationMain.SCREEN_SIMULATION);
            }
        });
        table.add(button);

        TextButton button2 = new TextButton("Default", uiSkin);
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSettings(FollowTheLeaderSimulation.getDefaultSettings());
                slider.setValue(settings.getNumberOfCars());
                sliderNOL.setValue(settings.getNumberOfLanes());
                sliderRL.setValue(settings.getRoadLength());
                sliderA.setValue(settings.getA());
                sliderFLUC.setValue(settings.getInitialFluctuation());
            }
        });
        table.add(button2);

        TextButton button3 = new TextButton("Cancel", uiSkin);
        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(TrafficSimulationMain.SCREEN_SIMULATION);
            }
        });
        table.add(button3);

        stage.addActor(table);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
