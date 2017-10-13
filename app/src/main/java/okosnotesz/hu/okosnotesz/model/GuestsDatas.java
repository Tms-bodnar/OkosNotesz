package okosnotesz.hu.okosnotesz.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.security.Guard;
import java.util.Comparator;

/**
 * Created by user on 2017.06.27..
 */

public class GuestsDatas implements Parcelable {

    private int id;
    private int conId;
    private String name;
    private String phone1;
    private String phone2;
    private String email1;
    private String email2;
    private String contact1;
    private String contact2;

    public GuestsDatas() {
    }

    public GuestsDatas(int id){
        this.id = id;
    }

    public GuestsDatas(String name) {
        this.name = name;
    }

    public GuestsDatas(String name, String phone1) {
        this.name = name;
        this.phone1 = phone1;
    }

    public GuestsDatas(int conId, String name, String phone1, String phone2, String email1, String email2, String contact1, String contact2) {
        this.conId = conId;
        this.name = name;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.email1 = email1;
        this.email2 = email2;
        this.contact1 = contact1;
        this.contact2 = contact2;
    }

    public GuestsDatas(Parcel in){
        readFromParcel(in);
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator(){
                public GuestsDatas createFromParcel(Parcel in){
                    return new GuestsDatas(in);
                }
                public GuestsDatas[] newArray(int size){
                    return new GuestsDatas[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(conId);
        dest.writeString(name);
        dest.writeString(phone1);
        dest.writeString(phone2);
        dest.writeString(email1);
        dest.writeString(email2);
        dest.writeString(contact1);
        dest.writeString(contact2);
    }

    private void readFromParcel(Parcel in){
        this.id = in.readInt();
        this.conId = in.readInt();
        this.name = in.readString();
        this.phone1 = in.readString();
        this.phone2 = in.readString();
        this.email1 = in.readString();
        this.email2 = in.readString();
        this.contact1 = in.readString();
        this.contact2 = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConId() {
        return conId;
    }

    public void setConId(int conId) {
        this.conId = conId;
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

    public static class SortByName implements Comparator<GuestsDatas> {

        public int compare(GuestsDatas o1, GuestsDatas o2) {

            return o1.name.compareTo(o2.getName());
        }
    }
}
