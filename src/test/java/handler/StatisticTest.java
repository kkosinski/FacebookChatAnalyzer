package handler;

import model.Participant;
import org.junit.Test;
import parser.HtmlChatParser;
import parser.JsonChatParser;

import java.text.ParseException;
import java.util.List;

public class StatisticTest
{
    private Statistic underTest;

    @Test
    public void getStatistics()
    {
        JsonChatParser parser = new JsonChatParser();
        //        HtmlChatParser parser = new HtmlChatParser();
        List<Participant> participants = parser.extractParticipants();

        underTest = new Statistic(participants, parser.getFirstMessage(), parser.getLastMessage());
        underTest.createParticipantStatistic();
    }
}
