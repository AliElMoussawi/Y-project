package backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class AuthenticationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int ID=0;
    private String username;
    private String password;

    private CopyOnWriteArrayList<String> followers;

    private CopyOnWriteArrayList<String> following;

    public AuthenticationDTO(String Username,String Password){
        ID+=1;
        this.username=Username;
        this.password=Password;
        this.followers=new CopyOnWriteArrayList<String>();
        this.following=new CopyOnWriteArrayList<String>();
    }
}