package domain;

import java.time.LocalDate;

public class BasicUser extends User {
    public BasicUser(String username, String password, String firstName, String lastName, String email, String user) {
        super(username, password, firstName, lastName, email, user);
    }

    public BasicUser (String username, String password, String firstName, String lastName, String email,
                      String type, String address, String phone, LocalDate birth)
    {
        super(username, password, firstName, lastName, email, type, address, phone, birth);
    }
}
