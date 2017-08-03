package okosnotesz.hu.okosnotesz.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2017.05.20..
 */

public class Treatments implements Parcelable{

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

    public Treatments(Parcel in) {
       readFromParcel(in);
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Treatments createFromParcel(Parcel in) {
                    return new Treatments(in);
                }

                public Treatments[] newArray(int size) {
                    return new Treatments[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(time);
        dest.writeInt(price);
        dest.writeInt(cost);
        dest.writeString(note);
    }

    private void readFromParcel(Parcel in) {

        id = in.readInt();
        name = in.readString();
        time = in.readInt();
        price = in.readInt();
        cost = in.readInt();
        note = in.readString();
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
