package okosnotesz.hu.okosnotesz.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2017.05.21..
 */

public class Reports implements Parcelable{

    private int id;
    private int expertId;
    private String treatmentId;
    private Long date;
    private String guestName;
    private String note;

    public Reports(){
    }

    public Reports(int id) {
        this.id = id;
    }

    public Reports(int expert, String treatment, Long date, String guest, String note) {
        this.expertId = expert;
        this.treatmentId = treatment;
        this.date = date;
        this.guestName = guest;
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
        treatmentId = in.readString();
        date = in.readLong();
        guestName = in.readString();
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
        dest.writeString(treatmentId);
        dest.writeLong(date);
        dest.writeString(guestName);
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

    public String getTreatment() {
        return treatmentId;
    }

    public void setTreatment(String treatmentId) {
        this.treatmentId = treatmentId;
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

    @Override
    public String toString() {
        return date+"";
    }
}
