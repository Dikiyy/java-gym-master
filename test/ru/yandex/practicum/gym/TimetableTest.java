package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    @Test
    public void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Monday
        Map<TimeOfDay, List<TrainingSession>> mondayMap = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondayMap.values().stream().mapToInt(List::size).sum());
        assertSame(singleTrainingSession, mondayMap.get(new TimeOfDay(13, 0)).getFirst());

        // Tuesday
        Map<TimeOfDay, List<TrainingSession>> tuesdayMap = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdayMap.isEmpty());
    }

    @Test
    public void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(
                groupAdult, coach, DayOfWeek.THURSDAY, new TimeOfDay(20, 0)
        );
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(
                groupChild, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)
        );
        TrainingSession thursdayChildTrainingSession = new TrainingSession(
                groupChild, coach, DayOfWeek.THURSDAY, new TimeOfDay(13, 0)
        );
        TrainingSession saturdayChildTrainingSession = new TrainingSession(
                groupChild, coach, DayOfWeek.SATURDAY, new TimeOfDay(10, 0)
        );

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Monday -> 1
        Map<TimeOfDay, List<TrainingSession>> monday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, monday.size());
        TimeOfDay mondayKey = monday.keySet().iterator().next();
        assertEquals(13, mondayKey.getHours());
        assertEquals(0, mondayKey.getMinutes());
        List<TrainingSession> mondaySessions = monday.get(mondayKey);
        assertEquals(1, mondaySessions.size());
        assertSame(mondayChildTrainingSession, mondaySessions.getFirst());

        // Thursday -> 2
        Map<TimeOfDay, List<TrainingSession>> thursday =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursday.size());

        Iterator<TimeOfDay> it = thursday.keySet().iterator();
        TimeOfDay firstKey = it.next();
        TimeOfDay secondKey = it.next();

        assertEquals(13, firstKey.getHours());
        assertEquals(0, firstKey.getMinutes());
        assertEquals(20, secondKey.getHours());
        assertEquals(0, secondKey.getMinutes());

        List<TrainingSession> thursdayAt13 = thursday.get(firstKey);
        assertEquals(1, thursdayAt13.size());
        assertSame(thursdayChildTrainingSession, thursdayAt13.getFirst());

        List<TrainingSession> thursdayAt20 = thursday.get(secondKey);
        assertEquals(1, thursdayAt20.size());
        assertSame(thursdayAdultTrainingSession, thursdayAt20.getFirst());

        // Tuesday -> empty
        Map<TimeOfDay, List<TrainingSession>> tuesday =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesday.isEmpty());
    }

    @Test
    public void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);
        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> mondayAt13 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)
        );
        assertEquals(1, mondayAt13.size());
        assertSame(singleTrainingSession, mondayAt13.get(0));

        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> mondayAt14 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0)
        );
        assertTrue(mondayAt14.isEmpty());
    }

    @Test
    public void testMultipleSessionsSameDaySameTime() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupA = new Group("Группа А (дети)", Age.CHILD, 60);
        Group groupB = new Group("Группа B (взрослые)", Age.ADULT, 90);

        TimeOfDay t1300 = new TimeOfDay(13, 0);

        TrainingSession s1 = new TrainingSession(groupA, coach, DayOfWeek.MONDAY, t1300);
        TrainingSession s2 = new TrainingSession(groupB, coach, DayOfWeek.MONDAY, t1300);


        timetable.addNewTrainingSession(s1);
        timetable.addNewTrainingSession(s2);


        Map<TimeOfDay, List<TrainingSession>> mondayMap =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        assertEquals(1, mondayMap.size(), "Должен быть один ключ времени (13:00)");
        List<TrainingSession> at13 = mondayMap.get(t1300);
        assertNotNull(at13, "Список для 13:00 не должен быть null");
        assertEquals(2, at13.size(), "В 13:00 должно быть две тренировки");


        assertSame(s1, at13.get(0));
        assertSame(s2, at13.get(1));


        List<TrainingSession> exact = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, t1300);
        assertEquals(2, exact.size(), "Точечный запрос 13:00 должен вернуть 2 тренировки");
        assertSame(s1, exact.get(0));
        assertSame(s2, exact.get(1));
    }

}


