package model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Represents chat member
 */
@Data
public class Participant
{
    private String name;
    private List<Message> messages;
    private Map<Character, Integer> mostUsedLetter;
    private Map<String, Integer> monthOfYear;
    private Map<String, Integer> dayOfYear;
    private Map<String, Integer> dayOfWeek;
    private Integer everyYearAverage;
    private Integer everyMonthAverage;
    private Integer everyDayAverage;
    private Float everyHourAverage;
    private Float everyMinuteAverage;
    private Float everySecondAverage;
}
