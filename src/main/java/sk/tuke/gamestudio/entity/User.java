package sk.tuke.gamestudio.entity;

import javax.persistence.*;

@Entity
@Table(name = "app_user")
public class User {

  @Id
  @GeneratedValue
  private int ident;

  @Column(unique = true)
  private String username;

  @Column(unique = true)
  private String email;

  private String password;

  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public User() {
  }

  public int getIdent() {
    return ident;
  }

  public void setIdent(int ident) {
    this.ident = ident;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "User{" +
            "ident=" + ident +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            '}';
  }
}
