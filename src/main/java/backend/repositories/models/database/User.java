package backend.models.database;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class User {

    private Long id;

    private String username;
    private String email;
    private String password;
    private String fullName;
    private String bio;
    private String location;
    private String website;
    private String profilePictureUrl;

}
