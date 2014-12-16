package com.thomasdh.trafficsimulation.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
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
        stage.getViewport().update(width, height, true);
        mainTable.setSize(stage.getWidth(), stage.getHeight());
        mainTable.setPosition(0, 0);
    }

    Table simulationSettingsTable;
    Table presetsTable;
    Table appearanceTable;

    Table mainTable;

    int currentVisible = 0;
    final int PRESET = 0, SIMULATION = 1, APPEARANCE = 2;


    @Override
    public void show() {

        if (getSettings() == null)
            settings = Presets.defaultPresets[0].getSettings().copy();

        callbacks = new ArrayList<ValueUpdateCallback>();

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ExtendViewport(minWidth, minHeight));
        Gdx.input.setInputProcessor(stage);

        simulationSettingsTable = generateSimulationSettingsTable(stage.getWidth(), stage.getHeight());
        presetsTable = generatePresetsTable(stage.getWidth(), stage.getHeight());
        appearanceTable = generateAppearanceTable(stage.getWidth(), stage.getHeight());

        mainTable = new Table();
        mainTable.setSize(stage.getWidth(), stage.getHeight());
        mainTable.setPosition(0, 0);

        final HorizontalGroup categoryGroup = new HorizontalGroup();
        categoryGroup.addActor(new TextButton("Presets", uiSkin) {{
            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    switch (currentVisible) {
                        case PRESET:
                            break;
                        case SIMULATION:
                            mainTable.getCell(simulationSettingsTable).setActor(presetsTable);
                            currentVisible = PRESET;
                            break;
                        case APPEARANCE:
                            mainTable.getCell(appearanceTable).setActor(presetsTable);
                            currentVisible = PRESET;
                            break;
                    }
                }
            });
        }});
        categoryGroup.addActor(new TextButton("Simulation", uiSkin) {{
            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    switch (currentVisible) {
                        case PRESET:
                            mainTable.getCell(presetsTable).setActor(simulationSettingsTable);
                            currentVisible = SIMULATION;
                            System.out.println("SIMULATION");
                            break;
                        case SIMULATION:
                            break;
                        case APPEARANCE:
                            mainTable.getCell(appearanceTable).setActor(simulationSettingsTable);
                            currentVisible = SIMULATION;
                            break;
                    }
                }
            });
        }});
        categoryGroup.addActor(new TextButton("Appearance", uiSkin) {{
            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    switch (currentVisible) {
                        case PRESET:
                            mainTable.getCell(presetsTable).setActor(appearanceTable);
                            currentVisible = APPEARANCE;
                            break;
                        case SIMULATION:
                            mainTable.getCell(simulationSettingsTable).setActor(appearanceTable);
                            currentVisible = APPEARANCE;
                            break;
                        case APPEARANCE:
                            break;
                    }
                }
            });
        }});
        mainTable.add(categoryGroup).row();

        mainTable.add(presetsTable).height(Value.percentHeight(0.9f, mainTable)).width(Value.maxWidth).row();

        HorizontalGroup group = new HorizontalGroup();
        TextButton button = new TextButton("Save & close", uiSkin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setCurrentSettings(getSettings());
                main.setScreen(TrafficSimulationMain.SCREEN_SIMULATION);
            }
        });
        group.addActor(button);

        TextButton button3 = new TextButton("Cancel", uiSkin);
        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(TrafficSimulationMain.SCREEN_SIMULATION);
            }
        });
        group.addActor(button3);
        mainTable.add(group);

        stage.addActor(mainTable);


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

    Table generateAppearanceTable(float width, float height) {
        Table table = new Table();
        table.setSize(width, height);
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
        return table;
    }

    Table generatePresetsTable(float width, float height) {
        Table table = new Table();
        table.setSize(width, height);
        for (int x = 0; x < Presets.defaultPresets.length; x++) {
            table.add(generatePresetButton(Presets.defaultPresets[x]));
            if (x == Presets.defaultPresets.length / 2)
                table.row();
        }
        return table;
    }

    Table generateSimulationSettingsTable(float width, float height) {
        Table simSettings = new Table(uiSkin);
        simSettings.setSize(width, height);
        addSetting(simSettings, "Number of cars", 0, 1000, 1f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getNumberOfCars();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setNumberOfCars(Math.round(setting));
            }
        });
        addSetting(simSettings, "Road length (m)", 0f, 10000f, 100f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getRoadLength();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setRoadLength(setting);
            }
        });
        addSetting(simSettings, "Initial fluctuation (m)", 0, 5, 0.05f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getInitialFluctuation();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setInitialFluctuation(setting);
            }
        });
        addSetting(simSettings, "Maximum acceleration (m/s^2)", 0f, 3f, 0.01f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getAccelerationA();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setAccelerationA(setting);
            }
        });
        addSetting(simSettings, "Desired deceleration (m/s^2)", 0f, 3f, 0.01f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getDecelerationB();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setDecelerationB(setting);
            }
        });
        addSetting(simSettings, "Time headway (s)", 0f, 2.5f, 0.1f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getT();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setT(setting);
            }
        });
        addSetting(simSettings, "Acceleration exponent", 0f, 6f, 0.2f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getDelta();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setDelta(setting);
            }
        });
        addSetting(simSettings, "Maximum speed (m/s)", 0f, 200f, 10f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getMaxSpeed();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setMaxSpeed(setting);
            }
        });
        addSetting(simSettings, "Simulations per second (/s)", 1, 50, 1, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getSimulationsPerSecond();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setSimulationsPerSecond(Math.round(setting));
            }
        });
        addSetting(simSettings, "Time multiplier", 0.5f, 10f, 0.5f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getSpeedMultiplier();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setSpeedMultiplier(setting);
            }
        });
        addSetting(simSettings, "Minimum distance S0 (m)", 0.5f, 10f, 0.5f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getJamDistanceSZero();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setJamDistanceSZero(setting);
            }
        });
        addSetting(simSettings, "Minimum distance S1 (m)", 0f, 100f, 0.5f, callbacks, new SettingObtainer() {
            @Override
            public float getSetting() {
                return getSettings().getJamDistanceSOne();
            }

            @Override
            public void setSetting(float setting) {
                getSettings().setJamDistanceSOne(setting);
            }
        });
        return simSettings;
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

        TextButton addButton = new TextButton(" + ", uiSkin);
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

        TextButton subtractButton = new TextButton(" - ", uiSkin);
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
                "Number of cars: " + Math.round(preset.getSettings().getNumberOfCars()) + "\n" +
                "Number of lanes: " + Math.round(preset.getSettings().getNumberOfLanes()) + "\n" +
                "Road length: " + Math.round(preset.getSettings().getRoadLength()) + "\n" +
                "Initial fluctuation: " + Math.round(preset.getSettings().getInitialFluctuation() * 100f) / 100f + "\n" +
                "Maximum acceleration: " + Math.round(preset.getSettings().getAccelerationA() * 100f) / 100f + "\n" +
                "Desired deceleration: " + Math.round(preset.getSettings().getDecelerationB() * 100f) / 100f + "\n" +
                "Time headway: " + Math.round(preset.getSettings().getT() * 10f) / 10f + "\n" +
                "Acceleration exponent: " + Math.round(preset.getSettings().getDelta() * 10f) / 10f + "\n" +
                "Maximum speed: " + Math.round(preset.getSettings().getMaxSpeed()) + "\n" +
                "Simulations per second: " + Math.round(preset.getSettings().getSimulationsPerSecond()) + "\n" +
                "Time multiplier: " + Math.round(preset.getSettings().getSpeedMultiplier() * 10f) / 10f + "\n" +
                "Minimum distance S0: " + Math.round(preset.getSettings().getJamDistanceSZero() * 10f) / 10f + "\n" +
                "Minimum distance S1: " + Math.round(preset.getSettings().getJamDistanceSOne() * 10f) / 10f + "\n";

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
