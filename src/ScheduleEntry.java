public class ScheduleEntry {
    private String disciplineName;
    private String date;
    private String lessonType;
    private String fromTime;
    private String toTime;


    public ScheduleEntry(String disciplineName, String date, String lessonType, String fromTime, String toTime) {
        this.disciplineName = disciplineName;
        this.date = date;
        this.lessonType = lessonType;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public String getDate() {
        return date;
    }

    public String getLessonType() {
        return lessonType;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToTime() {
        return toTime;
    }

}
