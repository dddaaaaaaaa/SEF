package domain;

public class User {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    public boolean attendEvent;
    public boolean createEvent;

    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAttendEvent() {
        return attendEvent;
    }

    public void setAttendEvent(boolean attendEvent) {
        this.attendEvent = attendEvent;
    }

    public boolean isCreateEvent() {
        return createEvent;
    }

    public void setCreateEvent(boolean createEvent) {
        this.createEvent = createEvent;
    }
}
