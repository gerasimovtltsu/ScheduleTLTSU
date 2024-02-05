import java.util.List;

public class ScheduleData {
    private List<ScheduleEntry> entries;

    public ScheduleData(List<ScheduleEntry> entries) {
        this.entries = entries;
    }

    public List<ScheduleEntry> getEntries() {
        return entries;
    }
}
