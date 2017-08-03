package okosnotesz.hu.okosnotesz.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2017.05.21..
 */

public class Products implements Parcelable{

    private int id;
    private String name;
    private int price;
    private int cost;
    private String note;

    public Products() {
    }


    public Products(int id) {
        this.id = id;
    }

    public Products(String name) {
        this.name = name;
    }

    public Products(String name, int price, int cost, String note) {
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.note = note;
    }

    public Products(Parcel in){
        readFromParcel(in);
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator(){
                public Products createFromParcel(Parcel in){
                    return new Products(in);
                }
                public Products[] newArray(int size){
                    return new Products[size];
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
        dest.writeInt(price);
        dest.writeInt(cost);
        dest.writeString(note);
    }

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        name = in.readString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
