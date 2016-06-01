package model;

import javax.persistence.*;

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
    @Transient
    private Boolean checked;

    public Skill() {
        checked = false;
    }

    public Skill(Integer identifier, String description, String name) {
        this.identifier = identifier;
        this.description = description;
        this.name = name;
        checked = false;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
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
