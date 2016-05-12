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
    @Column(name = "update")
    @Temporal(TemporalType.DATE)
    private Date update;
    @Column(name = "progress")
    private Double progress;
    @Column(name = "finish")
    private Boolean finished;

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

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date updateDate) {
        this.update = updateDate;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        if (progress < 0 || progress > 1) {
            throw new IllegalArgumentException("Progress should be in range(0,1)");
        }
        this.progress = progress;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
