import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LoadSchedule implements Runnable {
    private final ScheduleLoadListener listener;
    private final int teacherId;
    private final String startDateStr;
    private final String endDateStr;

    public LoadSchedule(ScheduleLoadListener listener, int teacherId, String startDateStr, String endDateStr) {
        this.listener = listener;
        this.teacherId = teacherId;
        this.startDateStr = startDateStr;
        this.endDateStr = endDateStr;
    }

    @Override
    public void run() {
        try {
            String scheduleURL = String.format("https://its.tltsu.ru/api/schedule/teacher?teacherId=%d&fromDate=%s&toDate=%s",
                    teacherId, startDateStr, endDateStr);
            InputStream is = new URL(scheduleURL).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            rd.close();

            ScheduleData scheduleData = parseScheduleJson(jsonText);

            listener.onScheduleLoaded(scheduleData);
        } catch (Exception e) {
            listener.onError(e);
        }
    }

    private ScheduleData parseScheduleJson(String jsonText) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonText);
        List<ScheduleEntry> entries = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String disciplineName = jsonObject.getString("disciplineName");
            String date = jsonObject.getString("date");
            String lessonType = jsonObject.getString("type");
            String fromTime = jsonObject.getString("fromTime");
            String toTime = jsonObject.getString("toTime");

            ScheduleEntry entry = new ScheduleEntry(disciplineName, date, lessonType, fromTime, toTime); // Create a ScheduleEntry object
            entries.add(entry);
        }
        return new ScheduleData(entries);
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
