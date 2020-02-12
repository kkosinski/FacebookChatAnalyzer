package model;

import lombok.Data;

/**
 * Represents messages interval in different units.
 */
@Data
public class TimePeriod
{
    private long totalDaysCount;
    private long totalMonthsCount;
    private float totalYearsCount;

    private long totalHoursCount;
    private long totalMinutesCount;
    private long totalSecondsCount;
}
