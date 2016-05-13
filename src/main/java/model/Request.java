package model;

import DAO.SkillDAO;
import org.hibernate.Session;
import util.HibernateUtil;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "request_person")
public class Request {
    @Id
    @Column(name = "request_person_id")
    private Integer identifier;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_request")
    private Date dateOfRequest;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_respond")
    private Date dateOfRespond;
    @Column(name = "description")
    private String description;
    @Column(name = "respond")
    private String respond;
    @Transient
    private List<Skill> skills;

    public Request() {
    }

    public Request(Integer identifier, Person person, Task task,
                   Date dateOfRequest, Date dateOfRespond, String description,
                   String respond) {
        this.identifier = identifier;
        this.person = person;
        this.task = task;
        this.dateOfRequest = dateOfRequest;
        this.dateOfRespond = dateOfRespond;
        this.description = description;
        this.respond = respond;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(Date dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public Date getDateOfRespond() {
        return dateOfRespond;
    }

    public void setDateOfRespond(Date dateOfRespond) {
        this.dateOfRespond = dateOfRespond;
    }

    public String getRespond() {
        return respond;
    }

    public void setRespond(String respond) {
        this.respond = respond;
    }

    public List<Skill> getSkills() {
        if (skills == null) {
            loadSkills();
        }
        return skills;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    private void loadSkills() {
        SkillDAO skillDAO = new SkillDAO();
        skills = skillDAO.getSkillsByPrimaryTask(task
                .getPrimaryTask().getIdentifier());
    }
}
