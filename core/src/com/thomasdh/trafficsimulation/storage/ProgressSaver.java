package com.thomasdh.trafficsimulation.storage;

/**
 * Created by Thomas on 21-11-2014 in project TrafficSimulation in project ${PROJECT_NAME}.
 */
public interface ProgressSaver {

    public void saveProgress(Progress progress);

    public Progress readProgress();

}
