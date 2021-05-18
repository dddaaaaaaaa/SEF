package domain;

import java.time.LocalDate;
import java.util.Date;

public class EventOrganizerUser extends User{

    private String occupation;
    public void setOccupation(String occupation)
    {
        this.occupation = occupation;
    }

    public String getOccupation()
    {
        return this.occupation;
    }

    public EventOrganizerUser(String username, String password, String firstName, String lastName, String email,
                              String type, String address, String phone, LocalDate birth)
    {
        super(username, password, firstName, lastName, email, type, address, phone, birth);
    }
}
