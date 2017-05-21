package okosnotesz.hu.okosnotesz.model;

import java.util.Date;

/**
 * Created by user on 2017.05.21..
 */

public class Sales {
    private int id;
    private Products product;
    private String guestName;
    private String note;
    private String date;

    public Sales(int id) {
        this.id = id;
    }

    public Sales(Products product, String guestName, String note, String date) {
        this.product = product;
        this.guestName = guestName;
        this.note = note;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
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
