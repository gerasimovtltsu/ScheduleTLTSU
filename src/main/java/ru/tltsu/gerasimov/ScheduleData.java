package ru.tltsu.gerasimov;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ScheduleData {
    private List<ScheduleEntry> entries;
}