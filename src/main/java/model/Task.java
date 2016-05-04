package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @Column(name = "task_id")
    private Integer identifier;
    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;
    @ManyToOne
    @JoinColumn(name = "primary_task_id")
    private PrimaryTask primaryTask;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_create")
    private Date createDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_deadline")
    private Date deadline;
    @Column(name = "priority")
    private Integer priority;
    @Column(name = "amount_of_primary_task")
    private Integer amount;
    @Column(name = "description")
    private String description;

    public Task() {
    }

    public Task(Integer identifier, Problem problem, PrimaryTask primaryTask,
                Date createDate, Date deadline, Integer priority,
                Integer amount, String description) {
        this.identifier = identifier;
        this.problem = problem;
        this.primaryTask = primaryTask;
        this.createDate = createDate;
        this.deadline = deadline;
        this.priority = priority;
        this.amount = amount;
        this.description = description;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public PrimaryTask getPrimaryTask() {
        return primaryTask;
    }

    public void setPrimaryTask(PrimaryTask primaryTask) {
        this.primaryTask = primaryTask;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
}
