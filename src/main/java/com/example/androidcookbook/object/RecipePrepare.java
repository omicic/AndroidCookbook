package com.example.androidcookbook.object;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipePrepare implements Parcelable {

    private String id;
    private String rec_id;
    private String ing_id;
    private String ing_qu;


    public String getIng_qu() {
        return ing_qu;
    }

    public void setIng_qu(String ing_qu) {
        this.ing_qu = ing_qu;
    }

    public RecipePrepare() {
        super();
        // TODO Auto-generated constructor stub
    }

    public RecipePrepare(String id, String recId, String ing_id, String ing_qu) {

        super();
        this.id = id;
        this.rec_id = recId;
        this.ing_id = ing_id;
        this.ing_qu = ing_qu;
    }

    public String getIng_id() {
        return ing_id;
    }

    public void setIng_id(String ing_id) {
        this.ing_id = ing_id;
    }

    public RecipePrepare(Parcel in) {

        String[] data = new String[4];
        in.readStringArray(data);
        this.id = data[0];
        this.rec_id = data[1];
        this.ing_id = data[2];
        this.ing_qu = data[3];
    }

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(new String[]{
                this.id, this.rec_id, this.ing_id, this.ing_qu
        });
    }

    public static final Creator CREATOR = new Creator() {

        @Override
        public RecipePrepare createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new RecipePrepare(in);
        }

        @Override
        public RecipePrepare[] newArray(int size) {
            // TODO Auto-generated method stub
            return new RecipePrepare[size];
        }

    };


}
