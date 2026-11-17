package org.example;

import org.example.exceptions.IllegalInputTimeSpanException;
import org.example.exceptions.IllegalScaleFactorException;
import org.example.exceptions.NegativeTimeSpanException;

import java.util.Objects;

/**
 * Клас TimeSpan представляє часовий інтервал у годинах та хвилинах.
 */
public class TimeSpan {

    private int hours;
    private int minutes;

    /**
     * Конструктор без аргументів.
     * Створює інтервал 0 годин 0 хвилин.
     */
    public TimeSpan() {
        this(0, 0);
    }

    /**
     * Конструктор з одним аргументом (загальна кількість хвилин).
     *
     * @param minutes загальна кількість хвилин (не може бути від'ємною)
     */
    public TimeSpan(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException("Minutes cannot be negative: " + minutes);
        }
        this.hours = minutes / 60;
        this.minutes = minutes % 60;
    }

    /**
     * Конструктор з двома аргументами (години та хвилини).
     *
     * @param hours   години (hours >= 0)
     * @param minutes хвилини (0 <= minutes <= 59)
     */
    public TimeSpan(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException(
                    "Invalid hours or minutes: hours=" + hours + ", minutes=" + minutes +
                    " (hours must be >= 0, minutes must be in range [0, 59])"
            );
        }
        this.hours = hours;
        this.minutes = minutes;
    }

    /**
     * Конструктор копіювання.
     *
     * @param span об'єкт TimeSpan для копіювання
     */
    public TimeSpan(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException("TimeSpan cannot be null");
        }
        this.hours = span.getHours();
        this.minutes = span.getMinutes();
    }

    /**
     * Повертає кількість повних годин без урахування хвилин.
     *
     * @return години
     */
    public int getHours() {
        return hours;
    }

    /**
     * Повертає хвилинну частину інтервалу без урахування годин.
     *
     * @return хвилини
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Додає вказану кількість годин і хвилин до поточного інтервалу.
     *
     * @param hours   години (hours >= 0)
     * @param minutes хвилини (0 <= minutes <= 59)
     */
    public void add(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException(
                    "Invalid hours or minutes: hours=" + hours + ", minutes=" + minutes
            );
        }
        int totalMinutes = getTotalMinutes() + hours * 60 + minutes;
        this.hours = totalMinutes / 60;
        this.minutes = totalMinutes % 60;
    }

    /**
     * Додає задану загальну кількість хвилин.
     *
     * @param minutes кількість хвилин (не може бути від'ємною)
     */
    public void add(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException("Minutes cannot be negative: " + minutes);
        }
        int totalMinutes = getTotalMinutes() + minutes;
        this.hours = totalMinutes / 60;
        this.minutes = totalMinutes % 60;
    }

    /**
     * Додає інший часовий інтервал.
     *
     * @param span об'єкт TimeSpan для додавання
     */
    public void add(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException("TimeSpan cannot be null");
        }
        add(span.getHours(), span.getMinutes());
    }

    /**
     * Віднімає вказаний часовий інтервал (години і хвилини) від поточного.
     * Якщо результат менше 0, викидається NegativeTimeSpanException.
     *
     * @param hours   години (hours >= 0)
     * @param minutes хвилини (0 <= minutes <= 59)
     */
    public void subtract(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException(
                    "Invalid hours or minutes: hours=" + hours + ", minutes=" + minutes
            );
        }
        int currentTotal = getTotalMinutes();
        int subTotal = hours * 60 + minutes;
        if (subTotal > currentTotal) {
            throw new NegativeTimeSpanException(
                    "Resulting TimeSpan would be negative. Current: " + currentTotal +
                    " minutes, subtract requested: " + subTotal + " minutes"
            );
        }
        int diff = currentTotal - subTotal;
        this.hours = diff / 60;
        this.minutes = diff % 60;
    }

    /**
     * Віднімає задану загальну кількість хвилин.
     * Якщо результат менше 0, викидається NegativeTimeSpanException.
     *
     * @param minutes кількість хвилин
     */
    public void subtract(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException("Minutes cannot be negative: " + minutes);
        }
        int currentTotal = getTotalMinutes();
        if (minutes > currentTotal) {
            throw new NegativeTimeSpanException(
                    "Resulting TimeSpan would be negative. Current: " + currentTotal +
                    " minutes, subtract requested: " + minutes + " minutes"
            );
        }
        int diff = currentTotal - minutes;
        this.hours = diff / 60;
        this.minutes = diff % 60;
    }

    /**
     * Віднімає переданий часовий інтервал від поточного.
     *
     * @param span об'єкт TimeSpan для віднімання
     */
    public void subtract(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException("TimeSpan cannot be null");
        }
        subtract(span.getHours(), span.getMinutes());
    }

    /**
     * Повертає тривалість інтервалу в годинах як дробове число.
     *
     * @return години як дробове число
     */
    public double getTotalHours() {
        return this.hours + (this.minutes / 60.0);
    }

    /**
     * Повертає повну тривалість інтервалу у хвилинах.
     *
     * @return тривалість у хвилинах
     */
    public int getTotalMinutes() {
        return this.hours * 60 + this.minutes;
    }

    /**
     * Збільшує поточний часовий інтервал у factor разів.
     *
     * @param factor коефіцієнт масштабування (factor > 0)
     */
    public void scale(int factor) {
        if (factor <= 0) {
            throw new IllegalScaleFactorException("Scale factor must be greater than 0: " + factor);
        }
        int totalMinutes = getTotalMinutes() * factor;
        this.hours = totalMinutes / 60;
        this.minutes = totalMinutes % 60;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSpan timeSpan = (TimeSpan) o;
        return hours == timeSpan.hours && minutes == timeSpan.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }

    @Override
    public String toString() {
        return hours + " год " + minutes + " хв";
    }
}
