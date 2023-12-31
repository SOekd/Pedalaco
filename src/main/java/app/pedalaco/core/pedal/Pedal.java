package app.pedalaco.core.pedal;

import app.pedalaco.core.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedal")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pedal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Double startX;

    private Double startY;

    private Double endX;

    private Double endY;

    private String title;

    @Lob
    private String description;

    private LocalDateTime date;

    @OneToOne(fetch = FetchType.EAGER) @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany(fetch = FetchType.EAGER) @JoinTable(name = "pedal_participants")
    private List<User> participants;

    // a imagem não é obrigatória, então é preciso verificar se ela existe!
    @Lob
    @Column(length = 1000000)
    private byte[] image;

}
