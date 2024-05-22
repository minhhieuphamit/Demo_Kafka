package hdbank.com.accountservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "to_email")
    private String to;
    private String toName;
    private String subject;
    private String content;
    private boolean status;
}
