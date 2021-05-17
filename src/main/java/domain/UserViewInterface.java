package domain;

public class UserViewInterface {
  public User user; //TODO make private

  public void setUser(User u)
  {
    this.user = u;
  }

  public User getUser()
  {
    return this.user;
  }
}
