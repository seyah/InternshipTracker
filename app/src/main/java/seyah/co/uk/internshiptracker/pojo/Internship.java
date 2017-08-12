package seyah.co.uk.internshiptracker.pojo;

import java.io.Serializable;

public class Internship implements Serializable {

    private int id;
    private String name;
    private ApplicationStatus status;

    public Internship(int id, String name, ApplicationStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
