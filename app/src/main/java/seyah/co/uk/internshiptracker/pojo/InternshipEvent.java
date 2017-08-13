package seyah.co.uk.internshiptracker.pojo;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.sql.Date;

public class InternshipEvent implements Serializable, Comparable {

    private int id;
    private String name;
    private int internshipId;
    private String description;
    private ApplicationStatus status;
    private Date date;
    private boolean notify;

    public InternshipEvent(int id, String name, int internshipId, String description, ApplicationStatus status, Date date, boolean notify) {
        this.id = id;
        this.name = name;
        this.internshipId = internshipId;
        this.description = description;
        this.status = status;
        this.date = date;
        this.notify = notify;
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

    public int getInternshipId() {
        return internshipId;
    }

    public void setInternshipId(int internshipId) {
        this.internshipId = internshipId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return ApplicationStatus.statusAsPercentage(this.getStatus()) > ApplicationStatus.statusAsPercentage(((InternshipEvent)o).getStatus()) ? 1 : 0;
    }
}
