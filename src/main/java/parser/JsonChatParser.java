package parser;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import handler.DateFormatter;
import model.Message;
import model.Participant;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonChatParser extends ChatParser
{
    private static final Gson GSON = new Gson();
    private static final String DATE = "timestamp_ms";
    private static final String AUTHOR = "sender_name";
    private static final String CONTENT = "content";

    public JsonChatParser()
    {
        dateFormatter = new DateFormatter();
    }

    private ArrayList<Map<String, String>> parseChat()
    {
        ArrayList<Map<String, String>> result = new ArrayList<>();

        for (String file : getChatFiles())
        {
            String target =
                Objects.requireNonNull(getClass().getClassLoader().getResource("messages/" + file)).getFile();

            try
            {
                if (file.endsWith(".json"))
                {
                    JsonReader reader = new JsonReader(new FileReader(target));
                    Map<String, ArrayList<Map<String, String>>> chatContent = GSON.fromJson(reader, Object.class);
                    result.addAll(chatContent.get("messages"));
                }
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    public List<Participant> extractParticipants()
    {
        List<Participant> result = new ArrayList<>();

        for (Map<String, String> message : parseChat())
        {
            setStartAndEnd(extractDate(String.valueOf(message.get(DATE))));

            Participant participant = result
                .stream()
                .filter(p -> p.getName().equals(message.get(AUTHOR)))
                .findAny()
                .orElse(new Participant());

            List<Message> participantMessages =
                participant.getMessages() != null ? participant.getMessages() : new ArrayList<>();

            String date = extractDate(String.valueOf(message.get(DATE)));
            String messageContent = message.get(CONTENT) == null ? "" : message.get(CONTENT);
            Message msg = new Message(date, messageContent);

            participantMessages.add(msg);
            participant.setName(participant.getName() == null ? message.get(AUTHOR) : participant.getName());
            participant.setMessages(participantMessages);
            participant.setMessages(participantMessages);

            if (!result.contains(participant))
            {
                result.add(participant);
            }
        }
        return result;
    }

    private String extractDate(String dateAsMillis)
    {
        String s = new BigDecimal(dateAsMillis).toEngineeringString();
        Timestamp ts = null;
        try
        {
            ts = new Timestamp(Double.valueOf(s).longValue());
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        String[] raw = new Date(Objects.requireNonNull(ts).getTime()).toString().split(" ");
        String month = raw[1];
        String day = raw[2];
        String hour = raw[3];
        String year = raw[5];

        String dateInExpectedFormat = new StringBuilder(month)
            .append(" ")
            .append(day)
            .append(", ")
            .append(year)
            .append(" ")
            .append(hour)
            .toString();

        return dateInExpectedFormat;
    }
}
