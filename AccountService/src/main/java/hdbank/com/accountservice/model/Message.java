package hdbank.com.accountservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String to;
    private String toName;
    private String subject;
    private String content;
}
