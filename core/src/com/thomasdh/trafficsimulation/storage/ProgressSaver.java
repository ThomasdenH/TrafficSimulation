package com.thomasdh.trafficsimulation.storage;

public interface ProgressSaver {

    public void saveProgress(Progress progress);

    public Progress readProgress();

}
