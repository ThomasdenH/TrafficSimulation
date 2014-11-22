package com.thomasdh.trafficsimulation.storage;

/**
 * Created by Thomas on 21-11-2014 in project TrafficSimulation.
 */
public interface ProgressSaver {

    public void saveProgress(Progress progress);

    public Progress readProgress();

}
