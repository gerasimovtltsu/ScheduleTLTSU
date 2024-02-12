package ru.tltsu.gerasimov;

public interface ScheduleLoadListener {
    void onScheduleLoaded(ScheduleData scheduleData);
    void onError(Exception e);
}