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
import com.thomasdh.trafficsimulation.objects.Presets;
import com.thomasdh.trafficsimulation.objects.SimulationSettings;

import java.util.ArrayList;

/**
 * Created by Thomas on 19-11-2014 in project TrafficSimulation.
 */
public class SettingsScreen implements Screen {

    private final TrafficSimulationMain main;

    private Stage stage;
    private final float minWidth = 700f;
    private final float minHeight = 700f;
    private Skin uiSkin;

    public SimulationSettings getSettings() {
        return settings;
    }

    private SimulationSettings settings;

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

        if (getSettings() == null)
            settings = Presets.defaultPresets[0].getSettings().copy();

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
                return getSettings().getNumberOfCars();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setNumberOfCars(Math.round(setting));
            }
        });
        addSetting(table, "Number of lanes", 1, 15, 1, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getNumberOfLanes();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setNumberOfLanes(Math.round(setting));
            }
        });
        addSetting(table, "Road length (m)", 0f, 10000f, 100f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getRoadLength();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setRoadLength(setting);
            }
        });
        addSetting(table, "Initial fluctuation (m)", 0, 5, 0.05f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getInitialFluctuation();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setInitialFluctuation(setting);
            }
        });
        addSetting(table, "Maximum acceleration (m/s^2)", 0f, 3f, 0.01f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getAccelerationA();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setAccelerationA(setting);
            }
        });
        addSetting(table, "Desired deceleration (m/s^2)", 0f, 3f, 0.01f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getDecelerationB();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setDecelerationB(setting);
            }
        });
        addSetting(table, "Time headway (s)", 0f, 2.5f, 0.1f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getT();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setT(setting);
            }
        });
        addSetting(table, "Acceleration exponent", 0f, 6f, 0.2f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getDelta();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setDelta(setting);
            }
        });
        addSetting(table, "Maximum speed (m/s)", 0f, 200f, 10f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getMaxSpeed();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setMaxSpeed(setting);
            }
        });
        addSetting(table, "Simulations per second (/s)", 1, 50, 1, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getSimulationsPerSecond();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setSimulationsPerSecond(Math.round(setting));
            }
        });
        addSetting(table, "Minimum distance S0 (m)", 0.5f, 10f, 0.5f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getJamDistanceSZero();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setJamDistanceSZero(setting);
            }
        });
        addSetting(table, "Minimum distance S1 (m)", 0f, 100f, 0.5f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getJamDistanceSOne();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setJamDistanceSOne(setting);
            }
        });


        TextButton button = new TextButton("Save & close", uiSkin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setCurrentSettings(getSettings());
                main.setScreen(TrafficSimulationMain.SCREEN_SIMULATION);
            }
        });
        table.add(button);

        TextButton button3 = new TextButton("Cancel", uiSkin);
        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(TrafficSimulationMain.SCREEN_SIMULATION);
            }
        });
        table.add(button3);
        table.row();
        for (Presets.Preset preset : Presets.defaultPresets) {
            table.add(generatePresetButton(preset));
        }

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

    private ArrayList<ValueUpdateCallback> callbacks;

    void addSetting(Table table, final String name, final float min, final float max, final float step, ArrayList<ValueUpdateCallback> callbacks, final SettingObtainer obtainer) {
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

    TextButton generatePresetButton(final Presets.Preset preset) {

        String text = preset.getName() + ":\n\n" +
                "Number of cars: " + preset.getSettings().getNumberOfCars() + "\n" +
                "Number of lanes: " + preset.getSettings().getNumberOfLanes() + "\n" +
                "Road length: " + preset.getSettings().getRoadLength() + "\n" +
                "Initial fluctuation: " + preset.getSettings().getInitialFluctuation() + "\n" +
                "Maximum acceleration: " + preset.getSettings().getAccelerationA() + "\n" +
                "Desired deceleration: " + preset.getSettings().getDecelerationB() + "\n" +
                "Time headway: " + preset.getSettings().getT() + "\n" +
                "Acceleration exponent: " + preset.getSettings().getDelta() + "\n" +
                "Maximum speed: " + preset.getSettings().getMaxSpeed() + "\n" +
                "Simulations per second: " + preset.getSettings().getSimulationsPerSecond() + "\n" +
                "Minimum distance S0: " + preset.getSettings().getJamDistanceSZero() + "\n" +
                "Minimum distance S1: " + preset.getSettings().getJamDistanceSOne() + "\n";

        TextButton textButton = new TextButton(text, uiSkin);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSettings(preset.getSettings().copy());
                for (ValueUpdateCallback callback : callbacks) {
                    callback.updateValue();
                }
            }
        });
        return textButton;
    }
}
