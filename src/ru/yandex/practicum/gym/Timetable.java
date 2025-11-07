package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {


    private final HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        timetable.putIfAbsent(dayOfWeek, new TreeMap<>());
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.computeIfAbsent(dayOfWeek, k -> new TreeMap<>());
        dayMap.putIfAbsent(timeOfDay, new ArrayList<>());
        dayMap.get(timeOfDay).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);
        if (dayMap == null || dayMap.isEmpty()) {
            return Collections.emptyList();
        }
        List<TrainingSession> result = new ArrayList<>();
        for (List<TrainingSession> trainingSessions : dayMap.values()) {
            result.addAll(trainingSessions);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);
        if (dayMap.isEmpty()) {
            return Collections.emptyList();
        }
        return dayMap.getOrDefault(timeOfDay, Collections.emptyList());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        HashMap<Coach, Integer> counters = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> dayMap : timetable.values()) {
            if (dayMap == null || dayMap.isEmpty()) {
                continue;
            }
            for (List<TrainingSession> listTrainingss : dayMap.values()) {
                for (TrainingSession session : listTrainingss) {
                    Coach coach = session.getCoach();
                    counters.merge(coach, 1, Integer::sum);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> e : counters.entrySet()) {
            result.add(new CounterOfTrainings(e.getKey(), e.getValue()));
        }

        result.sort(
                Comparator.comparingInt(CounterOfTrainings::getCount)
                        .reversed()
                        .thenComparing(ct -> ct.getCoach().toString())
        );

        return result;
    }
}
