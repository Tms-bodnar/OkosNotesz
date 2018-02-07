package hu.okosnotesz.okosnotesz.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2017.05.21..
 */

public class Reports implements Parcelable{

    private int id;
    private int expertId;
    private String expertName;
    private String treatmentNames;
    private Long date;
    private String guestName;
    private int conID;
    private int duration;
    private String note;

    public Reports(){
    }

    public Reports(int id) {
        this.id = id;
    }

    public Reports(int expert, String expertName, String treatment, Long date, String guest, int duration, int conID, String note) {
        this.expertId = expert;
        this.expertName = expertName;
        this.treatmentNames = treatment;
        this.date = date;
        this.guestName = guest;
        this.duration = duration;
        this.conID = conID;
        this.note = note;
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator(){
                public Object createFromParcel(Parcel in) {
                    return new Reports(in);
                }
                public Object[] newArray(int size) {
                    return new Reports[size];
                }
            };

    public Reports (Parcel in){
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        expertId = in.readInt();
        expertName = in.readString();
        treatmentNames = in.readString();
        date = in.readLong();
        guestName = in.readString();
        duration = in.readInt();
        conID = in.readInt();
        note = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(expertId);
        dest.writeString(expertName);
        dest.writeString(treatmentNames);
        dest.writeLong(date);
        dest.writeString(guestName);
        dest.writeInt(duration);
        dest.writeInt(conID);
        dest.writeString(note);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExpert() {
        return expertId;
    }

    public void setExpert(int expertId) {
        this.expertId = expertId;
    }

    public String getExpertname(){
        return expertName;
    }

    public void setExpertName(String expertName){
        this.expertName = expertName;
    }

    public String getTreatment() {
        return treatmentNames;
    }

    public void setTreatment(String treatmentId) {
        this.treatmentNames = treatmentId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getConID() {
        return conID;
    }

    public void setConID(int conID) {
        this.conID = conID;
    }

    @Override
    public String toString() {
        return guestName+", "+treatmentNames+ duration;
    }
}
