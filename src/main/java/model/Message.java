package model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents message containing date and content as plain text.
 */
@Data
@AllArgsConstructor
public class Message
{
    private String date;
    private String content;
}
