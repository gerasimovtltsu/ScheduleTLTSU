package ru.tltsu.gerasimov;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class ScheduleApp extends JFrame {

    private final JComboBox<String> teachersComboBox;
    private Map<String, Integer> teacherMap = new TreeMap<>();
    private final JTable scheduleTable;

    private LocalDate currentStartOfWeek;
    private LocalDate currentEndOfWeek;

    public ScheduleApp() {
        setTitle("Расписание ТГУ Тольятти");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Создаем панель с менеджером компоновки BorderLayout
        JPanel topPanel = new JPanel(new BorderLayout());


        teachersComboBox = new JComboBox<>(new Vector<>());
        JPanel teacherSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        teacherSelectionPanel.add(new JLabel("Преподаватель: "));
        teacherSelectionPanel.add(teachersComboBox);
        topPanel.add(teacherSelectionPanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton prevWeekBtn = new JButton("Пред. неделя");
        JButton nextWeekBtn = new JButton("След. неделя");
        JButton loadScheduleBtn = new JButton("Загрузить расписание");
        buttonPanel.add(prevWeekBtn);
        buttonPanel.add(nextWeekBtn);
        buttonPanel.add(loadScheduleBtn);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Таблица для расписания
        scheduleTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scheduleTable.setFillsViewportHeight(true);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setCurrentWeek(LocalDate.now());
        loadTeachersData();

        pack();

        // беру половину от экрана, чтобы окно было по центру
        setSize(screenSize.width / 2, screenSize.height / 2);
        setLocationRelativeTo(null);
        setVisible(true);

        // вешаю на кнопки эвенты по загрузке расписания
        loadScheduleBtn.addActionListener(e -> loadScheduleForSelectedTeacher());
        prevWeekBtn.addActionListener(e -> moveToPreviousWeek());
        nextWeekBtn.addActionListener(e -> moveToNextWeek());
    }

    private void loadTeachersData() {
        TeacherLoadListener listener = new TeacherLoadListener() {
            @Override
            public void onTeachersLoaded(Map<String, Integer> teachers) {
                SwingUtilities.invokeLater(() -> {
                    teacherMap.clear();
                    teacherMap.putAll(teachers);

                    Vector<String> sortedTeachers = new Vector<>(teacherMap.keySet());
                    sortedTeachers.sort(Comparator.naturalOrder());

                    teachersComboBox.setModel(new DefaultComboBoxModel<>(sortedTeachers));
                });
            }

            @Override
            public void onError(Exception e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(ScheduleApp.this,
                        "Ошибка загрузки информации по преподавателям:  " + e.getMessage(), "Ошибка",
                        JOptionPane.ERROR_MESSAGE));
            }
        };

        LoadTeachers loadTeachers = new LoadTeachers(listener);
        new Thread(loadTeachers).start();
    }

    private void loadTeacherSchedule(int teacherId, String startDateStr, String endDateStr) {
        ScheduleLoadListener listener = new ScheduleLoadListener() {
            @Override
            public void onScheduleLoaded(ScheduleData scheduleData) {
                SwingUtilities.invokeLater(() -> updateScheduleTable(scheduleData));
            }

            @Override
            public void onError(Exception e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(ScheduleApp.this,
                        "Ошибка загрузки расписания: " + e.getMessage(), "Ошибка",
                        JOptionPane.ERROR_MESSAGE));
            }
        };

        LoadSchedule loadSchedule = new LoadSchedule(listener, teacherId, startDateStr, endDateStr);
        new Thread(loadSchedule).start();
    }

    private String getFormattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return date.atStartOfDay(ZoneId.of("UTC")).format(formatter);
    }


    private void loadScheduleForSelectedTeacher() {
        String selectedTeacher = (String) teachersComboBox.getSelectedItem();
        if (selectedTeacher != null && teacherMap.containsKey(selectedTeacher)) {
            Integer teacherId = teacherMap.get(selectedTeacher);
            String startDateStr = getFormattedDate(currentStartOfWeek);
            String endDateStr = getFormattedDate(currentEndOfWeek);
            loadTeacherSchedule(teacherId, startDateStr, endDateStr);
        } else {
            JOptionPane.showMessageDialog(ScheduleApp.this,
                    "Пожалуйста, выберите преподавателя",
                    "Преподаватель не выбран", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateScheduleTable(ScheduleData scheduleData) {
        List<ScheduleEntry> scheduleEntries = scheduleData.getEntries();

        String[] columns = new String[] { "Дисциплина", "Тип занятия", "Время начала", "Время окончания" };
        Object[][] data = new Object[scheduleEntries.size()][columns.length];

        for (int i = 0; i < scheduleEntries.size(); i++) {
            ScheduleEntry entry = scheduleEntries.get(i);
            data[i][0] = entry.getDisciplineName();
            data[i][1] = entry.getLessonType();
            data[i][2] = parseDate(entry.getFromTime());
            data[i][3] = parseDate(entry.getToTime());
        }

        scheduleTable.setModel(new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private void setCurrentWeek(LocalDate date) {
        currentStartOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        currentEndOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        updateScheduleForCurrentWeek();
    }

    private void updateScheduleForCurrentWeek() {
        String startDateStr = currentStartOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE) + "T20:00:00.000Z";
        String endDateStr = currentEndOfWeek.format(DateTimeFormatter.ISO_LOCAL_DATE) + "T20:00:00.000Z";
        String selectedTeacher = (String) teachersComboBox.getSelectedItem();
        if (selectedTeacher != null && teacherMap.containsKey(selectedTeacher)) {
            Integer teacherId = teacherMap.get(selectedTeacher);
            if (teacherId != null) {
                loadTeacherSchedule(teacherId, startDateStr, endDateStr);
            }
        }
    }

    private void moveToPreviousWeek() {
        setCurrentWeek(currentStartOfWeek.minusWeeks(1));
    }

    private void moveToNextWeek() {
        setCurrentWeek(currentStartOfWeek.plusWeeks(1));
    }

    public static void main(String[] args) {
        try {
            // заставляем Swing перенимать интерфейс от API ОС
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Запуск GUI
        SwingUtilities.invokeLater(() -> new ScheduleApp());
    }

    private String parseDate(String dateToParse) {
        LocalDateTime dateTime = LocalDateTime.parse(dateToParse, DateTimeFormatter.ISO_DATE_TIME);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
        return dateTime.format(formatter);
    }
}