package app.pedalaco.core.privatemessage;

import app.pedalaco.core.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "private_message")
public class PrivateChat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @OneToMany
    private List<PrivateMessage> messages;

    public List<PrivateMessage> getOrderedMessagesBySent() {
        return messages.stream().sorted(Comparator.comparing(PrivateMessage::getSentAt)).toList();
    }

    public PrivateMessage getLastMessage() {
        return getOrderedMessagesBySent().get(messages.size() - 1);
    }

}
