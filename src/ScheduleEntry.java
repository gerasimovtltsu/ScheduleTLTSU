public class ScheduleEntry {
    private String disciplineName;
    private String date;
    private String fromTime;
    private String toTime;


    public ScheduleEntry(String disciplineName, String date, String fromTime, String toTime) {
        this.disciplineName = disciplineName;
        this.date = date;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public String getDate() {
        return date;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToTime() {
        return toTime;
    }

}
