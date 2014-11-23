package com.thomasdh.trafficsimulation.android;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.thomasdh.trafficsimulation.TrafficSimulationMain;
import com.thomasdh.trafficsimulation.objects.FollowTheLeaderCar;
import com.thomasdh.trafficsimulation.objects.SimulationSettings;
import com.thomasdh.trafficsimulation.storage.Progress;
import com.thomasdh.trafficsimulation.storage.ProgressSaver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AndroidLauncher extends AndroidApplication {

    private static final String progressFilePath = "progress.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new TrafficSimulationMain(new ProgressSaver() {
            @Override
            public void saveProgress(Progress progress) {
                try {
                    FileHandle handle = Gdx.files.local(progressFilePath);
                    ObjectOutputStream stream = new ObjectOutputStream(handle.write(false));
                    stream.writeObject(progress.settings);
                    stream.writeObject(progress.cars);
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Progress readProgress() {
                FileHandle handle = Gdx.files.local(progressFilePath);
                if (handle.exists()) {
                    try {
                        Progress progress = new Progress();
                        ObjectInputStream stream = new ObjectInputStream(handle.read());
                        progress.settings = (SimulationSettings) stream.readObject();
                        progress.cars = (ArrayList<FollowTheLeaderCar>) stream.readObject();
                        stream.close();
                        return progress;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                return null;
            }
        }), config);
    }
}
