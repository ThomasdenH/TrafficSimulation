package com.thomasdh.trafficsimulation;

import com.badlogic.gdx.Game;
import com.thomasdh.trafficsimulation.objects.SimulationSettings;
import com.thomasdh.trafficsimulation.screens.SettingsScreen;
import com.thomasdh.trafficsimulation.screens.SimulationScreen;
import com.thomasdh.trafficsimulation.storage.ProgressSaver;

public class TrafficSimulationMain extends Game {

    private SimulationScreen simulationScreen;
    private SettingsScreen settingsScreen;

    public final ProgressSaver progressSaver;

    public TrafficSimulationMain(ProgressSaver progressSaver) {
        this.progressSaver = progressSaver;
    }

    @Override
    public void create() {
        setScreen(SCREEN_SIMULATION);
    }

    public static final int SCREEN_SIMULATION = 1, SCREEN_SETTINGS = 2;

    public SimulationSettings getCurrentSettings() {
        return currentSettings;
    }

    public void setCurrentSettings(SimulationSettings currentSettings) {
        this.currentSettings = currentSettings;
    }

    private SimulationSettings currentSettings;

    public void setScreen(int screen) {
        if (screen == SCREEN_SIMULATION) {
            if (simulationScreen == null)
                simulationScreen = new SimulationScreen(this);
            setScreen(simulationScreen);
        } else if (screen == SCREEN_SETTINGS) {
            if (settingsScreen == null)
                settingsScreen = new SettingsScreen(this);
            settingsScreen.setSettings(currentSettings);
            setScreen(settingsScreen);
        } else {
            throw new NumberFormatException(screen + " is not a valid screen number");
        }
    }


}
