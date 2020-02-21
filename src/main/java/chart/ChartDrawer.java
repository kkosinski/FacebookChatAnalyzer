package chart;

import helper.DataSort;
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
            getChartData(sortByDayOfWeek(participant.getDayOfWeek()), participant.getName())));

        Map<String, ? extends Number> averageMessages =
            Map.of("year", participant.getAveragePerYear(), "month",
                participant.getAveragePerMonth(), "day", participant.getAveragePerDay(), "hour",
                participant.getAveragePerHour(), "minute", participant.getAveragePerMinute(),
                "second", participant.getAveragePerSecond());

        result.add(ChartFactory.createBarChart("MESSAGES AVERAGE", "", "",
            getChartData(averageMessages, participant.getName())));

        return result;
    }

    private DefaultCategoryDataset getChartData(Map<String, ? extends Number> data, String title)
    {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        for (Map.Entry<String, ? extends Number> entry : data.entrySet())
        {
            result.setValue(entry.getValue(), title, entry.getKey());
        }
        return result;
    }

    private Map<String, Integer> sortByDate(Map<String, Integer> target)
    {
        Map<String, Integer> result = new TreeMap<>(new DataSort().sortByDate());
        result.putAll(target);
        return result;
    }

    private Map<String, Integer> sortByDayOfWeek(Map<String, Integer> target)
    {
        Map<String, Integer> result = new TreeMap<>(new DataSort().sortByDayOfWeek());
        result.putAll(target);
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
