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
    private Integer averagePerYear;
    private Integer AveragePerMonth;
    private Integer averagePerDay;
    private Float averagePerHour;
    private Float averagePerMinute;
    private Float averagePerSecond;
}
