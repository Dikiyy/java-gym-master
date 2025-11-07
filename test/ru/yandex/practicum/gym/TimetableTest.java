package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Monday
        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, monday.size());
        assertSame(singleTrainingSession, monday.getFirst());

        // Tuesday
        List<TrainingSession> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesday.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Monday -> 1
        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, monday.size());
        assertSame(mondayChildTrainingSession, monday.getFirst());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursday.size());
        assertEquals(13, thursday.getFirst().getTimeOfDay().getHours());
        assertEquals(0,  thursday.getFirst().getTimeOfDay().getMinutes());
        assertEquals(20, thursday.get(1).getTimeOfDay().getHours());
        assertEquals(0,  thursday.get(1).getTimeOfDay().getMinutes());
        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesday.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
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

}


