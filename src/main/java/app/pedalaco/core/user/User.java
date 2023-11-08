package app.pedalaco.core.user;

import app.pedalaco.core.pedal.Pedal;
import app.pedalaco.core.privatemessage.PrivateChat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter @Setter
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    private String username;

    private String name;

    @JsonIgnore
    private String hashedPassword;

    private long experiencePoints;

    private String biography;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<UserRole> roles;

    @Lob
    @Column(length = 1000000)
    private byte[] profilePicture;

    @OneToMany(mappedBy = "author")
    private Set<PrivateChat> authoredChats;

    @OneToMany(mappedBy = "receiver")
    private Set<PrivateChat> receivedChats;

    public long getLevel() {
        // 10 * ((2 ^ n) - 1) = totalExp
        // onde n é o level, derivando:
        // n = log2(totalExp / 10 + 1)
        // como o log2 não existe no java, usamos a mudança de base:
        return (long) Math.floor(Math.log((experiencePoints / 10.0) + 1) / Math.log(2));
    }

    public long getExpToNextLevel() {
        return (long) Math.floor(10 * (int) Math.pow(2, getLevel() + 1.0) - 10.0 * (int) Math.pow(2, getLevel()));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return experiencePoints == user.experiencePoints && Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(name, user.name) && Objects.equals(hashedPassword, user.hashedPassword) && Objects.equals(biography, user.biography);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, username, name, hashedPassword, experiencePoints, biography);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", experiencePoints=" + experiencePoints +
                ", biography='" + biography + '\'' +
                '}';
    }
}
