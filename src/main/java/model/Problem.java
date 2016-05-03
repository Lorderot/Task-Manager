package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "problems")
public class Problem {
    @Id
    @Column(name = "problem_id")
    private Integer identifier;
    @ManyToOne
    @JoinColumn(name = "plane_id")
    private Plane plane;
    @ManyToOne
    @JoinColumn(name = "creator_person_id")
    private Person creator;
    @ManyToOne
    @JoinColumn(name = "assign_to_person_id")
    private Person assignTo;
    @Column(name = "description")
    private String description;
    @Column(name = "date_create")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Column(name = "date_update")
    @Temporal(TemporalType.DATE)
    private Date updateDate;
    @Column(name = "date_deadline")
    @Temporal(TemporalType.DATE)
    private Date deadline;
    @Column(name = "priority")
    private Integer priority;
    @Column(name = "short_name")
    private String name;
    @Column(name = "solved")
    private Boolean solved;

    public Problem() {
    }

    public Problem(Integer identifier, String description,
                   Date createDate, Date updateDate, Date deadline,
                   Integer priority, String name, Boolean solved) {
        this.identifier = identifier;
        this.description = description;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.deadline = deadline;
        this.priority = priority;
        this.name = name;
        this.solved = solved;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public Person getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(Person assignTo) {
        this.assignTo = assignTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSolved() {
        return solved;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }
}
