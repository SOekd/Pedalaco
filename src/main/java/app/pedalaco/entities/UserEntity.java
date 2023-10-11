package app.pedalaco.entities;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.Objects;

@Builder(access = AccessLevel.PUBLIC)
@Data
@AllArgsConstructor
public class UserEntity {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String biography;
    private long experiencePoints;
    private byte[] profilePicture;

}
