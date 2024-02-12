package ru.tltsu.gerasimov;

import lombok.Data;

@Data
public class ScheduleEntry {
    private final String disciplineName;
    private final String lessonType;
    private final String fromTime;
    private final String toTime;
}