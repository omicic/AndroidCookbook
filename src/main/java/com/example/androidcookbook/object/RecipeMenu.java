package com.example.androidcookbook.object;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeMenu implements Parcelable {

    private String menu_id;
    private String rec_id;
    private String day;
    private String check;
    private String mainmeal;

    public RecipeMenu() {
        super();
    }

    public RecipeMenu(String rec_id, String day, String check, String mainmeal) {
        this.rec_id = rec_id;
        this.day = day;
        this.check = check;
        this.mainmeal = mainmeal;
    }

    public RecipeMenu(Parcel in) {
        String[] data = new String[5];
        in.readStringArray(data);
        this.menu_id = data[0];
        this.rec_id = data[1];
        this.day = data[2];
        this.check = data[3];
        this.mainmeal = data[4];
    }

    public RecipeMenu(String menu_id, String rec_id, String day, String check, String mainmeal) {
        this.menu_id = menu_id;
        this.rec_id = rec_id;
        this.day = day;
        this.check = check;
        this.mainmeal = mainmeal;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.menu_id, this.rec_id, this.day, this.check, this.mainmeal
        });
    }

    public String getMainmeal() {
        return mainmeal;
    }

    public void setMainmeal(String mainmeal) {
        this.mainmeal = mainmeal;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public static final Creator CREATOR = new Creator() {

        @Override
        public Ingredient createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Ingredient[size];
        }

    };


}
