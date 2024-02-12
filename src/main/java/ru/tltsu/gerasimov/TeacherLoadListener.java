package ru.tltsu.gerasimov;

import java.util.Map;

public interface TeacherLoadListener  {
    void onTeachersLoaded(Map<String, Integer> teachers);
    void onError(Exception e);
}