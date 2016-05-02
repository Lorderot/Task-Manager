package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long identifier;
    @Column(name = "first_name", length = 100)
    private String firstName;
    @Column(name = "last_name", length = 100)
    private String lastName;
    @Column(name = "date_in")
    @Temporal(value = TemporalType.DATE)
    private Date dateIn;
    @Column(name = "date_out")
    @Temporal(value = TemporalType.DATE)
    private Date dateOut;
    @Column(name = "education")
    private String education;
    @Column(name = "login", length = 100)
    private String login;
    @Column(name = "password", length = 100)
    private String password;
    @Column(name = "salt", length = 10)
    private String salt;
    @Column(name = "birthday")
    @Temporal(value = TemporalType.DATE)
    private Date birthday;
    @Column(name = "phone_number")
    private Integer phone_number;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "skype", length = 100)
    private String skype;
    @Column(name = "access_type")
    private String userType;


    public Person(String firstName, String lastName, Date dateIn, Date dateOut,
                  String education, String login, String password, String salt,
                  Date birthday, Integer phone_number, String email,
                  String skype, String userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
        this.education = education;
        this.login = login;
        this.password = password;
        this.salt = salt;
        this.birthday = birthday;
        this.phone_number = phone_number;
        this.email = email;
        this.skype = skype;
        this.userType = userType;
    }

    public Person() {
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
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

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(Date dateOut) {
        this.dateOut = dateOut;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public UserType getUserType() {
        return UserType.convert(userType);
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
