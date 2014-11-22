package com.thomasdh.trafficsimulation.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.thomasdh.trafficsimulation.storage.Progress;
import com.thomasdh.trafficsimulation.storage.ProgressSaver;
import com.thomasdh.trafficsimulation.TrafficSimulationMain;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(480, 320);
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return new TrafficSimulationMain(new ProgressSaver() {
            @Override
            public void saveProgress(Progress progress) {

            }

            @Override
            public Progress readProgress() {
                return null;
            }
        });
    }
}