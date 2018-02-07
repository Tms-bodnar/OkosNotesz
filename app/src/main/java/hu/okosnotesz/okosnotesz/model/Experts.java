package hu.okosnotesz.okosnotesz.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2017.05.21..
 */

public class Experts implements Parcelable{

    private int id;
    private String name;
    private String note;

    public Experts() {
    }

    public Experts(int id) {
        this.id = id;
    }

    public Experts(String name, String note) {

        this.name = name;
        this.note = note;
    }

    public Experts(Parcel in){
        readFromParcel(in);
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator(){
                public Experts createFromParcel(Parcel in){
                    return new Experts(in);
                }
                public Experts[] newArray(int size){
                    return new Experts[size];
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
        dest.writeString(note);

    }

    public void readFromParcel(Parcel in){
        id = in.readInt();
        name = in.readString();
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
