package app.pedalaco.core.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
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



}
