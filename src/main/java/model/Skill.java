package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @Column(name = "skill_id")
    private Integer identifier;
    @Column(name = "description")
    private String description;
    @Column(name = "name")
    private String name;

    public Skill() {
    }

    public Skill(Integer identifier, String description, String name) {
        this.identifier = identifier;
        this.description = description;
        this.name = name;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
