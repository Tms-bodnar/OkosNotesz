package okosnotesz.hu.okosnotesz.model;

/**
 * Created by user on 2017.06.27..
 */

public class GuestsDatas {

    private int id;
    private String name;
    private String phone1;
    private String phone2;
    private String email1;
    private String email2;
    private String contact1;
    private String contact2;

    public GuestsDatas(){
    }

    public GuestsDatas(String name) {
        this.name = name;
    }

    public GuestsDatas(String name, String phone1) {
        this.name = name;
        this.phone1 = phone1;
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

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }
}
