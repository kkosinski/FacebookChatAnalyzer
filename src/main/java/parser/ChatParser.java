package parser;

import handler.DateFormatter;
import lombok.Data;

import java.io.File;
import java.net.URL;
import java.util.Date;

@Data
public class ChatParser
{
    Date firstMessage;
    Date lastMessage;
    DateFormatter dateFormatter;

    String[] getChatFiles()
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("messages");
        String path = url.getPath();
        return new File(path).list();
    }

    void setStartAndEnd(String date)
    {
        Date target = dateFormatter.parseDate(date);

        if (firstMessage == null || target.before(firstMessage))
        {
            firstMessage = target;
        }
        if (lastMessage == null || target.after(lastMessage))
        {
            lastMessage = target;
        }
    }
}
