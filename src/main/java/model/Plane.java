package model;

import javax.persistence.*;

@Entity
@Table(name = "planes")
public class Plane {
    @Id
    @Column(name = "plane_id")
    private Integer identifier;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "technical_status")
    private Boolean technicalStatus;
    @Column(name = "fuel_status")
    private Boolean fuelStatus;
    @Column(name = "type", length = 20)
    private String type;
    @Column(name = "available")
    private Boolean availability;
    @Column(name = "capacity")
    private Integer capacity;
    @Column(name = "owner", length = 100)
    private String owner;

    public Plane() {
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

    public Boolean getTechnicalStatus() {
        return technicalStatus;
    }

    public void setTechnicalStatus(Boolean technicalStatus) {
        this.technicalStatus = technicalStatus;
    }

    public Boolean getFuelStatus() {
        return fuelStatus;
    }

    public void setFuelStatus(Boolean fuelStatus) {
        this.fuelStatus = fuelStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean available) {
        this.availability = available;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
