package app.pedalaco.core.pedal;

import app.pedalaco.core.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pedal_message")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class PedalMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime date;

    private String message;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

}
