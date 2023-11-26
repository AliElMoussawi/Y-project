package backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.net.Socket;

@Getter
@Setter
@AllArgsConstructor
public class FollowClientDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private AuthenticationDTO user;
    private String user_to_follow;


    //table of users

}
