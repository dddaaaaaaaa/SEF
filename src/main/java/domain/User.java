package domain;

import java.time.LocalDate;
import java.util.Date;

public abstract class User {
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String email;
    public String user;
    private String address;
    private String phone;
    private LocalDate birth;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return this.address;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return this.phone;
    }

    public void setBirth(LocalDate birth)
    {
        this.birth = birth;
    }

    public LocalDate getBirth()
    {
        return this.birth;
    }

    //public long getBirthAsUnix()
    //{
    //    return birth.getTime() / 1000;
    //}

    public User(String username, String password, String firstName, String lastName, String email, String type) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.user = type;
        this.address = "";
        this.phone = "";
        this.birth = LocalDate.of(1970, 1, 1);
    }

    public User(String username, String password, String firstName, String lastName, String email, String type,
                String address, String phone, LocalDate birth) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.user = type;
        this.address = address;
        this.phone = phone;
        this.birth = birth;
    }
}
