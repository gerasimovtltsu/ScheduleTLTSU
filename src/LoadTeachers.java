import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class LoadTeachers implements Runnable {
    private final TeacherLoadListener listener;

    public LoadTeachers(TeacherLoadListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            InputStream is = new URL("https://its.tltsu.ru/api/teachers/department/64").openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONArray jsonArray = new JSONArray(jsonText);

            Map<String, Integer> teacherMap = new HashMap<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String fullName = jsonObject.getString("lastName") + " " +
                        jsonObject.getString("name") + " " +
                        jsonObject.getString("patronymic");
                int id = jsonObject.getInt("id");
                teacherMap.put(fullName, id);
            }

            listener.onTeachersLoaded(teacherMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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