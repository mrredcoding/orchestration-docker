package efrei.backend.contracts;

import efrei.backend.models.clients.RoleType;
import lombok.Getter;

@Getter
public class RegisterClient {

    private String lastName;
    private String firstName;
    private String email;
    private RoleType role;
    private String password;
}
