package com.thomasdh.trafficsimulation.screens;

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
import com.thomasdh.trafficsimulation.TrafficSimulationMain;
import com.thomasdh.trafficsimulation.objects.SimulationSettings;
import com.thomasdh.trafficsimulation.simulation.FollowTheLeaderSimulation;

import java.util.ArrayList;

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

        callbacks = new ArrayList<ValueUpdateCallback>();

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ExtendViewport(minWidth, minHeight));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table(uiSkin);
        table.setSize(stage.getWidth(), stage.getHeight());
        table.setPosition(0, 0);

        addSetting(table, "Number of cars", 0, 1000, 1f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return settings.getNumberOfCars();
            }

            @Override
            public void setSetting(float setting) {
                settings.setNumberOfCars(Math.round(setting));
            }
        });
        addSetting(table, "Number of lanes", 1, 15, 1, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return settings.getNumberOfLanes();
            }

            @Override
            public void setSetting(float setting) {
                settings.setNumberOfLanes(Math.round(setting));
            }
        });
        addSetting(table, "Road length (m)", 0f, 10000f, 100f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return settings.getRoadLength();
            }

            @Override
            public void setSetting(float setting) {
                settings.setRoadLength(setting);
            }
        });
        addSetting(table, "Initial fluctuation (m)", 0, 5, 0.05f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return settings.getInitialFluctuation();
            }

            @Override
            public void setSetting(float setting) {
                settings.setInitialFluctuation(setting);
            }
        });
        addSetting(table, "Maximum acceleration (m/s^2)", 0f, 3f, 0.01f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return settings.getAccelerationA();
            }

            @Override
            public void setSetting(float setting) {
                settings.setAccelerationA(setting);
            }
        });
        addSetting(table, "Desired deceleration (m/s^2)", 0f, 3f, 0.01f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return settings.getDecelerationB();
            }

            @Override
            public void setSetting(float setting) {
                settings.setDecelerationB(setting);
            }
        });
        addSetting(table, "Time headway (s)", 0f, 2.5f, 0.1f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return settings.getT();
            }

            @Override
            public void setSetting(float setting) {
                settings.setT(setting);
            }
        });
        addSetting(table, "Acceleration exponent", 0f, 6f, 0.2f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return settings.getDelta();
            }

            @Override
            public void setSetting(float setting) {
                settings.setDelta(setting);
            }
        });
        addSetting(table, "Maximum speed (m/s)", 0f, 200f, 10f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return settings.getMaxSpeed();
            }

            @Override
            public void setSetting(float setting) {
                settings.setMaxSpeed(setting);
            }
        });
        addSetting(table, "Simulations per second (/s)", 1, 50, 1, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return settings.getSimulationsPerSecond();
            }

            @Override
            public void setSetting(float setting) {
                settings.setSimulationsPerSecond(Math.round(setting));
            }
        });
        addSetting(table, "Minimum distance S0 (m)", 0.5f, 10f, 0.5f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return settings.getJamDistanceSZero();
            }

            @Override
            public void setSetting(float setting) {
                settings.setJamDistanceSZero(setting);
            }
        });
        addSetting(table, "Minimum distance S1 (m)", 0f, 100f, 0.5f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return settings.getJamDistanceSOne();
            }

            @Override
            public void setSetting(float setting) {
                settings.setJamDistanceSOne(setting);
            }
        });


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
                for (ValueUpdateCallback callback : callbacks) {
                    callback.updateValue();
                }
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

    ArrayList<ValueUpdateCallback> callbacks;

    public void addSetting(Table table, final String name, final float min, final float max, final float step, ArrayList<ValueUpdateCallback> callbacks, final SettingObtainer obtainer) {
        final Label labelA = new Label(name + ":   " + Math.round(obtainer.getSetting() / step) * step, uiSkin);
        table.add(labelA).width(300f);
        final Slider sliderA = new Slider(min, max, step, false, uiSkin);
        sliderA.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                obtainer.setSetting(sliderA.getValue());
                labelA.setText(name + ":   " + Math.round(Math.round(obtainer.getSetting() / step) * step * 10000f) / 10000f);
            }
        });

        sliderA.setValue(obtainer.getSetting());
        table.add(sliderA);

        TextButton addButton = new TextButton("+", uiSkin);
        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (obtainer.getSetting() < max) {
                    obtainer.setSetting(obtainer.getSetting() + step);
                    sliderA.setValue(obtainer.getSetting());
                }
            }
        });
        table.add(addButton);

        TextButton subtractButton = new TextButton("-", uiSkin);
        subtractButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (obtainer.getSetting() > min) {
                    obtainer.setSetting(obtainer.getSetting() - step);
                    sliderA.setValue(obtainer.getSetting());
                }
            }
        });
        table.add(subtractButton);

        table.row();

        callbacks.add(new ValueUpdateCallback() {
            @Override
            public void updateValue() {
                sliderA.setValue(obtainer.getSetting());
            }
        });
    }

    interface SettingObtainer {
        float getSetting();

        void setSetting(float setting);
    }

    interface ValueUpdateCallback {
        void updateValue();
    }
}
