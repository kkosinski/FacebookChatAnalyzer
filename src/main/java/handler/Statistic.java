package handler;

import chart.ChartDrawer;
import helper.DataSort;
import model.Message;
import model.Participant;
import model.TimePeriod;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Provides statistics for messaging for every chat participant.
 *
 * @author wintermute
 */
public class Statistic
{
    private List<Participant> participants;
    private Date startDate;
    private Date endDate;
    private TimePeriod timePeriod;
    private Map<Character, Integer> mostOftenUsedLetter;
    private DateFormatter dateFormatter;

    /**
     * Creates an instance
     *
     * @param participants of chat
     * @param firstMessage in analyzed chat to get beginning date
     * @param lastMessage in analyzed chat to get ending date
     */
    public Statistic(List<Participant> participants, Date firstMessage, Date lastMessage)
    {
        this.participants = participants;
        this.startDate = firstMessage;
        this.endDate = lastMessage;
        dateFormatter = new DateFormatter();
        calculateMessageTimeIntervals();
    }

    private void calculateMessageTimeIntervals()
    {
        timePeriod = new TimePeriod();
        timePeriod.setTotalDaysCount(ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant()));
        timePeriod.setTotalMonthsCount(
            ChronoUnit.MONTHS.between(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
        timePeriod.setTotalYearsCount(Math.round(
            Float.valueOf(ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant()) / Float.valueOf(365))
                * 100.0f) / 100.0f);
        timePeriod.setTotalHoursCount(ChronoUnit.HOURS.between(startDate.toInstant(), endDate.toInstant()));
        timePeriod.setTotalMinutesCount(ChronoUnit.MINUTES.between(startDate.toInstant(), endDate.toInstant()));
        timePeriod.setTotalSecondsCount(ChronoUnit.SECONDS.between(startDate.toInstant(), endDate.toInstant()));
    }

    /**
     * Creates statistics for every participant based on messages by date and count
     */
    public void createParticipantStatistic()
    {
        ChartDrawer dataChart = new ChartDrawer();
        final DataSort dataSort = new DataSort();
        SortedMap<String, Integer> dayOfYear;
        SortedMap<String, Integer> monthOfYear;
        SortedMap<String, Integer> dayOfWeek;

        for (Participant participant : participants)
        {
            List<Message> messages = participant.getMessages();
            dayOfYear = new TreeMap<>(dataSort.sortByDate());
            monthOfYear = new TreeMap<>(dataSort.sortByDayOfWeek());
            dayOfWeek = new TreeMap<>(dataSort.sortByDate());
            mostOftenUsedLetter = new HashMap<>();
            for (Message msg : messages)
            {
                try
                {
                    String[] date = dateFormatter.parseDate(msg.getDate()).toString().split(" ");
                    String month = date[1];
                    String day = date[2];
                    String year = date[5];

                    String period = month + " " + day + " " + year;
                    dayOfYear.merge(period, 1, Integer::sum);

                    period = dateFormatter.extractDayOfWeek(dateFormatter.extractDate(msg.getDate().replace(",", "")));
                    dayOfWeek.merge(period, 1, Integer::sum);

                    period = month + " " + year;
                    monthOfYear.merge(period, 1, Integer::sum);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                mostOftenUsedChar(msg.getContent());
            }
            participant.setDayOfYear(dayOfYear);
            participant.setMonthOfYear(monthOfYear);
            participant.setDayOfWeek(dayOfWeek);
            participant.setMostUsedLetter(mostOftenUsedLetter);
            calculateAverageStatistics(participant, messages.size());
            dataChart.drawChart(participant);
        }
    }

    private void mostOftenUsedChar(String content)
    {
        int maxOccurring = 0;
        for (int i = 0; i < content.length(); i++)
        {
            char letter = content.charAt(i);

            mostOftenUsedLetter.merge(letter, 1, Integer::sum);

            if (mostOftenUsedLetter.get(letter) > maxOccurring)
            {
                maxOccurring = mostOftenUsedLetter.get(letter);
            }
        }
    }

    private void calculateAverageStatistics(Participant participant, int messagesSum)
    {
        participant.setEveryYearAverage(messagesSum / (int) timePeriod.getTotalYearsCount());
        participant.setEveryMonthAverage(messagesSum / (int) timePeriod.getTotalMonthsCount());
        participant.setEveryDayAverage(messagesSum / (int) timePeriod.getTotalDaysCount());
        participant.setEveryHourAverage(messagesSum / Float.valueOf(timePeriod.getTotalHoursCount()));
        participant.setEveryMinuteAverage(messagesSum / Float.valueOf(timePeriod.getTotalMinutesCount()));
        participant.setEverySecondAverage(messagesSum / Float.valueOf(timePeriod.getTotalSecondsCount()));
    }
}
