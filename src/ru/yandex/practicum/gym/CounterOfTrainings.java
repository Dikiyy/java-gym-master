package ru.yandex.practicum.gym;

public final class CounterOfTrainings {
    private final Coach coach;
    private final int count;

    public CounterOfTrainings(Coach coach, int count) {
        this.coach = coach;
        this.count = count;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return coach + " -> " + count;
    }
}
