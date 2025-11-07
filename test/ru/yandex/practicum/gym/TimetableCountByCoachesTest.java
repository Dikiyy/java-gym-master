package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimetableCountByCoachesTest {

    private static TrainingSession Group(Group group, Coach coach, DayOfWeek dayOfWeek, int hours, int minutes) {
        return new TrainingSession(group, coach, dayOfWeek, new TimeOfDay(hours, minutes));
    }

    @Test
    void returnsEmptyWhenNoSessions() {
        Timetable timetable = new Timetable();
        List<CounterOfTrainings> res = timetable.getCountByCoaches();
        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    @Test
    void countsAndSortsByDescendingCount() {
        Timetable t = new Timetable();

        Group child = new Group("Детская", Age.CHILD, 60);
        Group adult = new Group("Взрослая", Age.ADULT, 90);

        Coach c1 = new Coach("Иванов", "Иван", "Иванович");     // 3
        Coach c2 = new Coach("Петров", "Пётр", "Петрович");     // 1
        Coach c3 = new Coach("Сидоров", "Сидор", "Сидорович");  // 2

        // Иванов3
        t.addNewTrainingSession(Group(child, c1, DayOfWeek.MONDAY, 10, 0));
        t.addNewTrainingSession(Group(adult, c1, DayOfWeek.WEDNESDAY, 12, 0));
        t.addNewTrainingSession(Group(child, c1, DayOfWeek.FRIDAY, 18, 30));

        // Петров1
        t.addNewTrainingSession(Group(adult, c2, DayOfWeek.MONDAY, 9, 0));

        // Сидоров2
        t.addNewTrainingSession(Group(child, c3, DayOfWeek.TUESDAY, 11, 0));
        t.addNewTrainingSession(Group(adult, c3, DayOfWeek.THURSDAY, 19, 0));

        List<CounterOfTrainings> res = t.getCountByCoaches();
        assertEquals(3, res.size());

        //Иванов 3 Сидоров 2, Петров 1
        assertEquals("Иванов Иван Иванович", res.get(0).getCoach().toString());
        assertEquals(3, res.get(0).getCount());

        assertEquals("Сидоров Сидор Сидорович", res.get(1).getCoach().toString());
        assertEquals(2, res.get(1).getCount());

        assertEquals("Петров Пётр Петрович", res.get(2).getCoach().toString());
        assertEquals(1, res.get(2).getCount());
    }

    @Test
    void tieBreaksByCoachFullNameAscending() {
        Timetable t = new Timetable();

        Group g = new Group("Группа", Age.ADULT, 60);

        // 2 - 2
        Coach a = new Coach("Александров", "Алексей", "Алексеевич");
        Coach b = new Coach("Борисов", "Борис", "Борисович");

        t.addNewTrainingSession(Group(g, b, DayOfWeek.MONDAY, 10, 0));
        t.addNewTrainingSession(Group(g, b, DayOfWeek.WEDNESDAY, 10, 0));

        t.addNewTrainingSession(Group(g, a, DayOfWeek.TUESDAY, 10, 0));
        t.addNewTrainingSession(Group(g, a, DayOfWeek.THURSDAY, 10, 0));

        List<CounterOfTrainings> res = t.getCountByCoaches();
        assertEquals(2, res.size());
        // Alex first then Boris
        assertEquals("Александров Алексей Алексеевич", res.get(0).getCoach().toString());
        assertEquals("Борисов Борис Борисович", res.get(1).getCoach().toString());
        assertEquals(2, res.get(0).getCount());
        assertEquals(2, res.get(1).getCount());
    }

    @Test
    void gettersAndToStringWork() {
        Coach coach = new Coach("Torebek", "Diar", "Azamatovich");
        CounterOfTrainings c = new CounterOfTrainings(coach, 5);

        assertSame(coach, c.getCoach());
        assertEquals(5, c.getCount());
        assertTrue(c.toString().contains("Torebek Diar Azamatovich"));
        assertTrue(c.toString().contains("5"));
    }
}
