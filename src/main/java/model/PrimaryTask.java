package model;

import util.TimeUtil;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "primary_tasks")
public class PrimaryTask {
    @Id
    @Column(name = "primary_task_id")
    private Integer identifier;
    @Column(name = "short_name")
    private String name;
    @Column(name = "cost")
    private Integer cost;
    @Column(name = "time_to_complete")
    private Long timeToComplete;
    @Column(name = "description")
    private String description;

    public PrimaryTask(Integer identifier, String name, Integer cost, Long timeToComplete,
                       String description) {
        this.identifier = identifier;
        this.name = name;
        this.cost = cost;
        this.timeToComplete = timeToComplete;
        this.description = description;
    }

    public PrimaryTask() {
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Time getTimeToComplete() {
        return new Time(timeToComplete - TimeUtil.TIME_OFFSET);
    }

    public void setTimeToComplete(Long timeToComplete) {
        this.timeToComplete = timeToComplete + TimeUtil.TIME_OFFSET;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
