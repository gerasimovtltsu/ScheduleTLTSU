import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScheduleData {
    // Example fields, adjust according to actual data
    private List<ScheduleEntry> entries;

    public ScheduleData(List<ScheduleEntry> entries) {
        this.entries = entries;
    }

    public List<ScheduleEntry> getEntries() {
        return entries;
    }

    private List<ScheduleEntry> parseScheduleJson(String jsonText) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonText);
        List<ScheduleEntry> scheduleEntries = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String disciplineName = jsonObject.getString("disciplineName");
            String fromTime = jsonObject.getString("fromTime");
            String toTime = jsonObject.getString("toTime");
            String date = jsonObject.getString("date");
            // ... other fields as needed

            scheduleEntries.add(new ScheduleEntry(disciplineName, date, fromTime, toTime));
        }

        return scheduleEntries;
    }
}
