package okosnotesz.hu.okosnotesz.model;

/**
 * Created by user on 2017.05.20..
 */

public class Treatments {

    private int id;
    private String name;
    private int time;
    private int price;
    private int cost;
    private String note;

    public Treatments() {
    }

    public Treatments(int id) {
        this.id = id;
    }

    public Treatments(String name, int time, int price, int cost, String note) {
        this.name = name;
        this.time = time;
        this.price = price;
        this.cost = cost;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        if(name!=null) {
            return name;
        }else
            return "No name";
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
