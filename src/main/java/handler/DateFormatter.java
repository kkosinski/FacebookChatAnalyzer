package handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatter
{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy MMM dd hh:mm", Locale.US);

    /**
     * Parses dates to consistent format.
     *
     * @param date to reformat to desired format
     * @return date containing necessary information to create statistics
     */
    public Date parseDate(String date)
    {
        Date result = null;
        try
        {
            String[] datePart = date.replace(",", "").split(" ");
            String month = datePart[0];
            String day = datePart[1];
            String year = datePart[2];
            String hour = datePart[3];

            result = DATE_FORMAT.parse(year + " " + month + " " + day + " " + hour);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    private String extractMonth(String month)
    {
        try
        {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MMM", Locale.US);
            Calendar cal = Calendar.getInstance();
            cal.setTime(inputFormat.parse(month));
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM", Locale.US); // 01-12
            return outputFormat.format(cal.getTime());
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return month;
    }

    /**
     * @param date parts as string to extract a date of it (example: Jun 2019 -> Thu 01 06 00:00:00 2019)
     * @return
     */
    public Date extractDate(String date)
    {
        int day = 0, month, year;
        String[] datePart = date.split(" ");

        month = Integer.valueOf(extractMonth(datePart[0])) - 1;
        if (datePart.length == 3)
        {
            day = Integer.valueOf(datePart[1]);
            year = Integer.valueOf(datePart[2]);
        } else
        {
            year = Integer.valueOf(datePart[1]);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        if (day != 0)
        {
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }
}
