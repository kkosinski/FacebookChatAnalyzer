package helper;

import handler.DateFormatter;

import java.time.DayOfWeek;
import java.util.Comparator;

public class DataSort
{
    private DateFormatter dateFormatter = new DateFormatter();

    public Comparator<String> sortByDate(){
        return (o1, o2) ->
        {
            if (dateFormatter.extractDate(o1).before(dateFormatter.extractDate(o2)))
            {
                return -1;
            } else if (dateFormatter.extractDate(o1).after(dateFormatter.extractDate(o2)))
            {
                return +1;
            } else
            {
                return 0;
            }
        };
    }

    public Comparator<String> sortByDayOfWeek() {
        return (o1, o2) ->
        {
            if (DayOfWeek.valueOf(o2.toUpperCase()).ordinal() > DayOfWeek.valueOf(o1.toUpperCase()).ordinal()) {
                return -1;
            } else if (DayOfWeek.valueOf(o1.toUpperCase()).ordinal() > DayOfWeek.valueOf(o2.toUpperCase()).ordinal()){
                return +1;
            } else {
                return 0;
            }

        };
    }
}
