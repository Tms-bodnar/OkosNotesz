package okosnotesz.hu.okosnotesz.model;

import java.util.Date;

/**
 * Created by user on 2017.05.21..
 */

public class Reports {

    private int id;
    private Experts expert;
    private Treatments treatment;
    private Date date;
    private String guestName;
    private String note;

    public Reports(int id) {
        this.id = id;
    }

    public Reports(Experts expert, Treatments treatment, Date date, String guest, String note) {
        this.expert = expert;
        this.treatment = treatment;
        this.date = date;
        this.guestName = guest;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Experts getExpert() {
        return expert;
    }

    public void setExpert(Experts expert) {
        this.expert = expert;
    }

    public Treatments getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatments treatment) {
        this.treatment = treatment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
