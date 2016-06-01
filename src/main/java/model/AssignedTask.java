package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "assigned_tasks")
public class AssignedTask {
    @Id
    @Column(name = "assigned_task_id")
    private Integer identifier;
    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    @Column(name = "date_of_assignment")
    @Temporal(TemporalType.DATE)
    private Date assignmentDate;
    @Column(name = "date_finish")
    @Temporal(TemporalType.DATE)
    private Date finishDate;
    @Column(name = "finish")
    private Boolean finished;
    @Column(name = "amount")
    private Integer amount;
    @Column(name = "description")
    private String description;

    public AssignedTask() {
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Date getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(Date assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
