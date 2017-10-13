package okosnotesz.hu.okosnotesz.model;

/**
 * Created by user on 2017.05.21..
 */

public class Sales {
    private int id;
    private int productID;
    private String guestName;
    private String note;
    private String date;
    private int quantity;
    private int value;

    public Sales(){}

    public Sales(int id) {
        this.id = id;
    }

    public Sales(int productID, String guestName, String note, String date) {
        this.productID = productID;
        this.guestName = guestName;
        this.note = note;
        this.date = date;
    }

    public Sales(int productID, String guestName, String note, String date, int quantity, int value) {
        this.productID = productID;
        this.guestName = guestName;
        this.note = note;
        this.date = date;
        this.quantity = quantity;
        this.value = value;
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

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getGuestName() + ", "+ getProductID() + ", "+getNote() + ", "+getDate() + ", "+ getValue() + ", "+ getQuantity();
    }
}
