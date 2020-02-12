package parser;

import handler.DateFormatter;
import model.Message;
import model.Participant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Parses HTML file and extracts Participants with their messages.
 *
 * @author wintermute
 */

public class HtmlChatParser extends ChatParser
{
    private static final String MESSAGE = "pam _3-95 _2pi0 _2lej uiBoxWhite noborder";

    public HtmlChatParser()
    {
        dateFormatter = new DateFormatter();
    }

    private Elements getChatContent(String filename)
    {
        String file = Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).getFile();
        Document content = null;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(file)))
        {
            String contentAsString = br.readLine();
            content = Jsoup.parse(contentAsString);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Objects.requireNonNull(content).getElementById("bluebarRoot").remove();
        return content.getElementsByClass(MESSAGE);
    }

    private Elements getAllMessages()
    {
        Elements result = new Elements();
        for (String file : getChatFiles())
        {
            if (file.endsWith(".html"))
            {
                result.addAll(getChatContent("messages/" + file));
            }
        }
        return result;
    }

    /**
     * @return participants with their messages.
     */
    public List<Participant> extractParticipants()
    {
        Elements allMessages = getAllMessages();
        String date = "_3-94 _2lem";
        String messageContent = "_3-96 _2let";
        String author = "_3-96 _2pio _2lek _2lel";
        List<Participant> result = new ArrayList<>();
        Participant participant;

        for (Element message : allMessages)
        {
            if (message.getElementsByClass(date).size() > 0 && message.getElementsByClass(author).size() > 0)
            {
                setStartAndEnd(message.getElementsByClass(date).text());

                participant = result
                    .stream()
                    .filter(p -> p.getName().equals(message.getElementsByClass(author).text()))
                    .findAny()
                    .orElse(new Participant());

                List<Message> participantMessages =
                    participant.getMessages() != null ? participant.getMessages() : new ArrayList<>();

                Message msg = new Message(message.getElementsByClass(date).get(0).text(),
                    message.getElementsByClass(messageContent).text());
                participantMessages.add(msg);
                participant.setName(participant.getName() == null ? message.getElementsByClass(author).get(0).text()
                                                                  : participant.getName());
                participant.setMessages(participantMessages);

                if (!result.contains(participant))
                {
                    result.add(participant);
                }
            }
        }
        return result;
    }
}
