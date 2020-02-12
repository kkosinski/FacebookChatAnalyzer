package chart;

import handler.DateFormatter;
import model.Participant;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Builds charts for data
 *
 * @author wintermute
 */
public class ChartDrawer
{
    private static final String DATA_DIR = "participants";

    public void drawChart(Participant participant)
    {
        List<JFreeChart> dataCharts = collectCharts(participant);

        for (JFreeChart chart : dataCharts)
        {
            try
            {
                prepareEnvironment(DATA_DIR, participant.getName());
                ChartUtils.saveChartAsPNG(
                    new File(DATA_DIR + "/" + participant.getName() + "/" + chart.getTitle().getText() + ".png"), chart,
                    450, 400);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    private List<JFreeChart> collectCharts(Participant participant)
    {
        List<JFreeChart> result = new ArrayList<>();

        result.add(ChartFactory.createBarChart("MESSAGES EVERY MONTH OF YEAR", "", "",
            getChartData(sortByDate(participant.getMonthOfYear()), participant.getName())));

        result.add(ChartFactory.createBarChart("MESSAGES EVERY DAY OF YEAR", "", "",
            getChartData(sortByDate(participant.getDayOfYear()), participant.getName())));

        result.add(ChartFactory.createBarChart("MESSAGES EVERY DAY OF WEEK", "", "",
            getChartData(getMessagePerDayOfWeek(participant.getDayOfWeek()), participant.getName())));

        //TODO:
        //        data.setTitle("MESSAGES AVERAGE PER YEAR");
        //        data.setTitle("MESSAGES AVERAGE PER MONTH");
        //        data.setTitle("MESSAGES AVERAGE PER DAY");
        //        data.setTitle("MESSAGES AVERAGE PER HOUR");
        //        data.setTitle("MESSAGES AVERAGE PER MINUTE");
        //        data.setTitle("MESSAGES AVERAGE PER SECOND");

        return result;
    }

    private DefaultCategoryDataset getChartData(Map<String, Integer> data, String title)
    {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        for (String key : data.keySet())
        {
            result.setValue(data.get(key), title, key);
        }
        return result;
    }

    private Map<String, Integer> sortByDate(Map<String, Integer> target)
    {
        DateFormatter df = new DateFormatter();
        Map<String, Integer> result = new TreeMap<>((o1, o2) ->
        {
            if (df.extractDate(o1).before(df.extractDate(o2)))
            {
                return -1;
            } else if (df.extractDate(o1).after(df.extractDate(o2)))
            {
                return +1;
            } else
            {
                return 0;
            }
        });
        result.putAll(target);
        return result;
    }

    private Map<String, Integer> getMessagePerDayOfWeek(Map<String, Integer> messageProDayOfWeek)
    {
        final String[] weekDays = new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        Map<String, Integer> result = new HashMap<>();

        for (String weekDay : weekDays)
        {
            result.put(weekDay, messageProDayOfWeek.get(weekDay));
        }

        return result;
    }

    private void prepareEnvironment(String... path)
    {
        Path dir;
        for (String target : path)
        {
            if (!DATA_DIR.equals(target))
            {
                dir = Paths.get(DATA_DIR + "/" + target);
            } else
            {
                dir = Paths.get(target);
            }
            if (!Files.exists(dir))
            {
                try
                {
                    Files.createDirectory(dir);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
