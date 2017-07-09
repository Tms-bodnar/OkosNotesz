package okosnotesz.hu.okosnotesz.model;

/**
 * Created by user on 2017.05.21..
 */

public class Experts {

    private int id;
    private String name;
    private String note;

    public Experts() {
    }

    public Experts(int id) {
        this.id = id;
    }

    public Experts(String name, String note) {
        this.id = id;
        this.name = name;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
